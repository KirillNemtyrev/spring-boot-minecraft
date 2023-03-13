package com.community.server.utils;

import com.community.server.dto.ClientDto;
import com.community.server.dto.FileDto;
import com.community.server.dto.ServerDto;
import com.community.server.query.MCQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MD5Files {

    private final List<FileDto> files = new ArrayList<>();
    private final List<String> folders = new ArrayList<>();

    public ServerDto[] getServers() {

        File file = new File("servers.json");
        ReadFile readFile = new ReadFile(file);
        ServerDto[] serverDto = new Gson().fromJson(readFile.get(), new TypeToken<ServerDto[]>() {}.getType());
        for (int count = 0; count < serverDto.length; count++){
            MCQuery mcQuery = new MCQuery(serverDto[count].getIp(), serverDto[count].getPort());
            serverDto[count].setOnline(mcQuery.fullStat().getOnlinePlayers());
            mcQuery.close();
        }
        return serverDto;
    }

    public void generate() throws IOException, NoSuchAlgorithmException {
        generate("loader");
    }

    public void generate(String path) throws IOException, NoSuchAlgorithmException {

        File file = new File(path);
        for (File item : Objects.requireNonNull(file.listFiles())) {

            if (item.isDirectory()) {

                generate(item.getPath());
                folders.add(item.getPath());

            } else {

                FileDto fileDto = new FileDto();
                fileDto.setPath(item.getPath());
                fileDto.setSize(item.length());
                fileDto.setMd5(getHash(item.getAbsolutePath()));

                files.add(fileDto);
            }
        }
    }

    @SneakyThrows
    public void input(String launcher) {

        File folder = new File("launcher/indexes");
        if(!folder.isDirectory() && !folder.mkdir()){
            return;
        }
        folders.sort(Comparator.comparingInt(String::length));

        ClientDto clientDto = new ClientDto();
        clientDto.setFiles(files);
        clientDto.setFolders(folders);

        File file = new File("launcher/indexes/" + launcher + ".json");
        if(!file.isFile() && !file.createNewFile()){
            return;
        }
        WriteFile writeFile = new WriteFile(file);
        writeFile.write(new Gson().toJson(clientDto));

        folders.clear();
        files.clear();

    }

    @SneakyThrows
    public void inputLoader() {
        folders.sort(Comparator.comparingInt(String::length));

        ClientDto clientDto = new ClientDto();
        clientDto.setFiles(files);
        clientDto.setFolders(folders);

        File file = new File("loader.json");
        if(!file.isFile() && !file.createNewFile()){
            return;
        }
        WriteFile writeFile = new WriteFile(file);
        writeFile.write(new Gson().toJson(clientDto));

        folders.clear();
        files.clear();
    }

    public String getHash(String fileName) throws IOException, NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(fileName);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        fis.close();
        return sb.toString();
    }

}
