package com.community.server.utils;

import com.community.server.dto.ClientDto;
import com.community.server.dto.FileDto;
import com.community.server.dto.ServerDto;
import com.community.server.query.MCQuery;
import com.community.server.query.QueryRequest;
import com.community.server.query.QueryResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.apache.catalina.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MD5Files {

    private List<FileDto> files = new ArrayList<>();
    private List<String> folders = new ArrayList<>();

    public ServerDto[] getServers() {

        File file = new File("servers.json");
        ReadFile readFile = new ReadFile(file);

        ServerDto[] servers = new Gson().fromJson(readFile.get(), new TypeToken<ServerDto[]>() {}.getType());
        for (int count = 0; count < servers.length; count++) {
            MCQuery mcQuery = new MCQuery(servers[count].getIp(), servers[count].getPort());
            QueryResponse queryResponse = mcQuery.basicStat();
            if (queryResponse == null) {
                continue;
            }

            servers[count].setOnline(queryResponse.getOnlinePlayers());
            servers[count].setPlayers(queryResponse.getMaxPlayers());
        }

        return servers;
    }

    public void generate(String path) throws IOException, NoSuchAlgorithmException {

        try {
            File file = new File(path);
            for (File item : file.listFiles()) {

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
        } catch (IOException | NoSuchAlgorithmException e){
        }
    }

    @SneakyThrows
    public void input(String launcher) {

        File folder = new File("launcher\\indexes");
        if(!folder.isDirectory() && !folder.mkdir()){
            return;
        }
        folders.sort(Comparator.comparingInt(String::length));

        ClientDto clientDto = new ClientDto();
        clientDto.setFiles(files);
        clientDto.setFolders(folders);
        clientDto.setCountFiles(files.size());
        clientDto.setCountFolders(folders.size());

        File file = new File("launcher\\indexes\\" + launcher + ".json");
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
