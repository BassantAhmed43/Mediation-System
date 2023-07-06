/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.bsmalah;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.apache.commons.net.ftp.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.util.*;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.util.io.pem.PemWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.Socket;

public class Bsmalah {

    public static void main(String[] args) {
        
        try {
            Class.forName("org.postgresql.Driver");
         
          Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/med","aya","123");
            System.out.println("Connected");

            Statement stmt1 = con.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT source_server_id FROM rules;");
            while (rs1.next()) {
                int source_server_id = rs1.getInt("source_server_id");
                Statement stmt20 = con.createStatement();
                    ResultSet rs20 = stmt20.executeQuery("SELECT server FROM downstream where id= "+ source_server_id+";");
                    while (rs20.next()) {
                    String server = rs20.getString("server");
                    Statement stmt40 = con.createStatement();
                    ResultSet rs40 = stmt40.executeQuery("SELECT username FROM downstream where id= "+ source_server_id+";");
                        while (rs40.next()) {
                            String username = rs40.getString("username"); 
                    Statement stmt400 = con.createStatement();
                    ResultSet rs400 = stmt400.executeQuery("SELECT directory FROM downstream where id= "+ source_server_id+";");
                        while (rs400.next()) {
                            String directory = rs400.getString("directory"); 
                    Statement stmt60 = con.createStatement();
                    ResultSet rs60 = stmt60.executeQuery("SELECT password FROM downstream where id= "+ source_server_id+";");
                        while (rs60.next()) {
                            String password = rs60.getString("password");
                    Statement stmt70 = con.createStatement();
                    ResultSet rs70 = stmt70.executeQuery("SELECT port FROM downstream where id= "+ source_server_id+";");
                        while (rs70.next()) {
                            int port = rs70.getInt("port");
                    Statement stmtd5000 = con.createStatement();
                    ResultSet rsd5000 = stmtd5000.executeQuery("SELECT ip FROM downstream where id= "+ source_server_id+";");
                        while (rsd5000.next()) {
                            String ip = rsd5000.getString("ip");         
                    
                    ///////////////////////////////////////////////////////
                    Statement stmt600 = con.createStatement();        
                    ResultSet rs600 = stmt600.executeQuery("SELECT destination_server_id FROM rules where source_server_id= "+ source_server_id+";");
                        while (rs600.next()) {
                            int destination_server_id = rs600.getInt("destination_server_id"); 
                    Statement stmtd20 = con.createStatement();
                    ResultSet rsd20 = stmtd20.executeQuery("SELECT server FROM upstream where id= "+ destination_server_id+";");
                    while (rsd20.next()) {
                    String dserver = rsd20.getString("server");
                    Statement stmtd40 = con.createStatement();
                    ResultSet rsd40 = stmtd40.executeQuery("SELECT username FROM upstream where id= "+ destination_server_id+";");
                        while (rsd40.next()) {
                            String dusername = rsd40.getString("username"); 
                    Statement stmtd400 = con.createStatement();
                    ResultSet rsd400 = stmtd400.executeQuery("SELECT directory FROM upstream where id= "+ destination_server_id+";");
                        while (rsd400.next()) {
                            String ddirectory = rsd400.getString("directory"); 
                            
                    Statement stmtd4000 = con.createStatement();
                    ResultSet rsd4000 = stmtd4000.executeQuery("SELECT ip FROM upstream where id= "+ destination_server_id+";");
                        while (rsd4000.next()) {
                            String dip = rsd4000.getString("ip"); 
                            
                    Statement stmtd60 = con.createStatement();
                    ResultSet rsd60 = stmtd60.executeQuery("SELECT password FROM upstream where id= "+ destination_server_id+";");
                        while (rsd60.next()) {
                            String dpassword = rsd60.getString("password");
                    Statement stmtd70 = con.createStatement();
                    ResultSet rsd70 = stmtd70.executeQuery("SELECT port FROM upstream where id= "+ destination_server_id+";");
                        while (rsd70.next()) {
                            int dport = rsd70.getInt("port"); 
                   ////////////////////////////////////////////////////////////////////////////
                   
                if(server.equals("ftp") && dserver.equals("scp")){
                    List<String> filePathList = getFilePathsOnFTPServer(server, port, username, password, directory);
                     for (String filePath : filePathList) {
                        String type = getFileExtension(filePath);
                        String filename = getFileNameFromPath(filePath);
                        downloadFilesFromFtpServer( ip,  port,  username,  password, directory,  "/home/Aya/Downloads/cdr/"+filename);
                        deleteFileOnFTPServer( ip,  port,  username,  password,  filePath);                       
                        if(type.equals("pem")){
                            translateASN1ToCDR("/home/Aya/Downloads/cdr/"+filename);
                            uploadToScpServer( dusername,  dip,  dport,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }
                        else{
                            translateCDRToASN1("/home/Aya/Downloads/cdr/"+filename);
                            uploadToScpServer( dusername,  dip,  dport,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }

                    
                     }
                }
                else if(server.equals("ftp") && dserver.equals("ftp")){
                    List<String> filePathList = getFilePathsOnFTPServer(server, port, username, password, directory);
                     for (String filePath : filePathList) {
                        String type = getFileExtension(filePath);
                        String filename = getFileNameFromPath(filePath);
                        downloadFilesFromFtpServer( ip,  port,  username,  password, directory,  "/home/Aya/Downloads/cdr/"+filename);
                        deleteFileOnFTPServer( ip,  port,  username,  password,  filePath);                       
                        if(type.equals("pem")){
                            translateASN1ToCDR("/home/Aya/Downloads/cdr/"+filename);
                            uploadFilesToFtpServer( dip,  dport,  dusername,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }
                        else{
                            translateCDRToASN1("/home/Aya/Downloads/cdr/"+filename);
                            uploadFilesToFtpServer( dip,  dport,  dusername,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }

                    
                     }
                }
                else if(server.equals("scp") && dserver.equals("stp")){
                    List<String> filePathList = listFilesOnSCPServer(ip,  port,  username,  password,  directory);
                     for (String filePath : filePathList) {
                        String type = getFileExtension(filePath);
                        String filename = getFileNameFromPath(filePath);
                        downloadFilesFromScpServer( username,  ip,  port,  password, directory,  "/home/Aya/Downloads/cdr/"+filename);                  
                        deleteFileOnSCPClient( ip,  port,  username,  password,  filePath);                        
                        if(type.equals("pem")){
                            translateASN1ToCDR("/home/Aya/Downloads/cdr/"+filename);
                            uploadFilesToFtpServer( dip,  dport,  dusername,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }
                        else{
                            translateCDRToASN1("/home/Aya/Downloads/cdr/"+filename);
                            uploadFilesToFtpServer( dip,  dport,  dusername,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }

                    
                     }
                }
                else if(server.equals("scp") && dserver.equals("ftp")){
                    List<String> filePathList = listFilesOnSCPServer(ip,  port,  username,  password,  directory);
                     for (String filePath : filePathList) {
                        String type = getFileExtension(filePath);
                        String filename = getFileNameFromPath(filePath);
                        downloadFilesFromScpServer( username,  ip,  port,  password, directory,  "/home/Aya/Downloads/cdr/");                  
                        deleteFileOnSCPClient( ip,  port,  username,  password,  filePath);  
                         System.out.println(filePath);
                        if(type.equals("pem")){
                            translateASN1ToCDR("/home/Aya/Downloads/cdr/"+filename);
                            uploadFilesToFtpServer( dip,  dport,  dusername,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }
                        else{
                            translateCDRToASN1("/home/Aya/Downloads/cdr/"+filename);
                            uploadFilesToFtpServer( dip,  dport,  dusername,  dpassword, "/home/Aya/Downloads/cdr/",  ddirectory);

                        }

                    
                     }
                }
                        }}}}}}}}}}}}}

                }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
  
    }

 


//    public static List<String> listFilesOnSCPServer(String serverIp, int port, String username, String password, String remoteFilePath) {
//        List<String> fileNames = new ArrayList<>();
//
//        try {
//            JSch jsch = new JSch();
//            Session session = jsch.getSession(username, serverIp, port);
//            session.setPassword(password);
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.connect();
//
//            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
//            sftp.connect();
//
//            // Get a list of all the files in the specified path
//            Vector<ChannelSftp.LsEntry> entries = sftp.ls(remoteFilePath);
//
//            // Extract file names excluding "." and ".." entries
//            for (ChannelSftp.LsEntry entry : entries) {
//                String filename = entry.getFilename();
//                if (filename.equals(".") || filename.equals("..")) {
//                    continue; // Skip the current and parent directory entries
//                }
//                fileNames.add(filename);
//            }
//
//            sftp.disconnect();
//            session.disconnect();
//        } catch (JSchException | SftpException e) {
//            e.printStackTrace();
//        }
//
//        return fileNames;
//    }
    
        public static List<String> listFilesOnSCPServer(String serverIp, int port, String username, String password, String remoteFilePath) {
    List<String> fileNames = new ArrayList<>();

    try {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, serverIp, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect();

        // Get a list of all the files in the specified path
        Vector<ChannelSftp.LsEntry> entries = sftp.ls(remoteFilePath);

        // Extract file names excluding "." and ".." entries
        for (ChannelSftp.LsEntry entry : entries) {
            String filename = entry.getFilename();
            if (filename.equals(".") || filename.equals("..")) {
                continue; // Skip the current and parent directory entries
            }
            
            // Create the full path by concatenating the remoteFilePath and the filename
            String fullPath = remoteFilePath + "/" + filename;
            fileNames.add(fullPath);
        }

        sftp.disconnect();
        session.disconnect();
    } catch (JSchException | SftpException e) {
        e.printStackTrace();
    }

    return fileNames;
}




//    ________

    static void uploadFile(FTPClient ftpClient, File file) throws IOException {
        String fileName = file.getName();
        try (InputStream inputStream = new FileInputStream(file)) {
            boolean isUploaded = ftpClient.storeFile(fileName, inputStream);
            if (isUploaded) {
                System.out.println("File Uploaded: " + fileName);
            } else {
                System.out.println("Failed to upload file: " + fileName);
            }
        }
    }

    static void downloadFromFolder(ChannelSftp channelSftp, String remoteFolderPath, String localFolderPath)
            throws SftpException {
        Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(remoteFolderPath);

        for (ChannelSftp.LsEntry en : entries) {
            if (en.getFilename().equals(".") || en.getFilename().equals("..") || en.getAttrs().isDir()) {
                continue;
            }

            String remoteFilePath = remoteFolderPath + en.getFilename();
            String localFilePath = localFolderPath + en.getFilename();

            channelSftp.get(remoteFilePath, localFilePath);
            System.out.println("Downloaded: " + en.getFilename());
        }
    }

    public static void fetchCDRDataFTP(String username, String password, String server, String remoteFilePath, String localFilePath, String type) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server);
            ftpClient.login(username, password);

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
            ftpClient.retrieveFile(remoteFilePath, outputStream);
            outputStream.close();

            ftpClient.logout();
            ftpClient.disconnect();

            System.out.println("file fetched and saved at: " + localFilePath);
            try {
                Class.forName("org.postgresql.Driver");
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/med", "aya", "123");
                System.out.println("Connected");
                LocalDateTime currentDateTime = getCurrentDateTime();
                Statement stmt = con.createStatement();
                String queryString = "insert into logs values ('" + currentDateTime + "' ,'" + server + "' , '" + remoteFilePath + "' ,'" + localFilePath + "' )";
                stmt.executeUpdate(queryString);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Bsmalah.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fetchCDRDataSCP(String username, String serverIp, String password, String remoteFilePath, String localFilePath, String type) {
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(username, serverIp, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("exec");
            String command = "cat " + remoteFilePath;
            ((ChannelExec) channel).setCommand(command);

            InputStream commandOutput = channel.getInputStream();
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(commandOutput));
            BufferedWriter writer = new BufferedWriter(new FileWriter(localFilePath));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            reader.close();
            writer.close();
            channel.disconnect();
            session.disconnect();

            System.out.println("file fetched and saved at: " + localFilePath);
            try {
                Class.forName("org.postgresql.Driver");
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/med", "aya", "123");
                System.out.println("Connected");
                LocalDateTime currentDateTime = getCurrentDateTime();
                Statement stmt = con.createStatement();
                String queryString = "insert into logs values ('" + currentDateTime + "' ,'" + serverIp + "' , '" + remoteFilePath + "' ,'" + localFilePath + "' )";
                stmt.executeUpdate(queryString);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Bsmalah.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void translateCDRToASN1(String cdrFilePath) {
        try {
            // Read the content of the CDR file
            FileInputStream fis = new FileInputStream(cdrFilePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_16BE));
            StringBuilder cdrContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                cdrContent.append(line);
            }
            reader.close();

            // Convert CDR content to ASN.1
            byte[] cdrBytes = cdrContent.toString().getBytes(StandardCharsets.UTF_16BE);
            ASN1Primitive asn1Data = new ASN1InputStream(cdrBytes).readObject();

            // Output the ASN.1 data as PEM format
            PemObject pemObject = new PemObject("ASN1 DATA", asn1Data.getEncoded());
            StringWriter pemStringWriter = new StringWriter();
            PemWriter pemWriter = new PemWriter(pemStringWriter);
            pemWriter.writeObject(pemObject);
            pemWriter.close();

            // Save the translated ASN.1 data to a text file
            String asn1FilePath = createCDRFilePath(cdrFilePath);
            saveCDRToFile(pemStringWriter.toString(), asn1FilePath);
            try {
                Class.forName("org.postgresql.Driver");
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/med", "aya", "123");
                System.out.println("Connected");
                Statement stmt = con.createStatement();
                LocalDateTime currentDateTime = getCurrentDateTime();
                String queryString = "insert into logs values ( '" + currentDateTime + "' , 'CDR to ANS1' ,'" + cdrFilePath + "' ,'" + asn1FilePath + "' )";
                stmt.executeUpdate(queryString);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Bsmalah.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void translateASN1ToCDR(String asn1FilePath) {
        try {
            // Read the content of the ASN.1 file
            BufferedReader reader = new BufferedReader(new FileReader(asn1FilePath));
            StringBuilder asn1Content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                asn1Content.append(line).append(System.lineSeparator());
            }
            reader.close();

            // Print the ASN.1 content
            System.out.println("ASN.1 content: ");
            System.out.println(asn1Content.toString());

            // Extract the ASN.1 data from the ASN.1 format
            String asn1Data = asn1Content.toString()
                    .replace("-----BEGIN ASN1 DATA-----", "")
                    .replace("-----END ASN1 DATA-----", "")
                    .replaceAll("\\s+", "");

            if (!asn1Data.isEmpty()) {
                try {
                    // Convert ASN.1 data back to CDR content
                    byte[] decodedData = Base64.getDecoder().decode(asn1Data);
                    ASN1Primitive cdrObject = new ASN1InputStream(decodedData).readObject();
                    String cdrContent = new String(cdrObject.getEncoded(), StandardCharsets.UTF_8);

                    // Reformat the CDR content with line breaks
                    StringBuilder formattedCDRContent = new StringBuilder();
                    String[] cdrLines = cdrContent.split(System.lineSeparator());
                    for (String cdrLine : cdrLines) {
                        formattedCDRContent.append(cdrLine.trim()).append(System.lineSeparator());
                    }

                    // Save the translated CDR content to a text file
                    String cdrFilePath = createASN1FilePath(asn1FilePath);
                    saveCDRToFile(formattedCDRContent.toString(), cdrFilePath);
                    try {
                        Class.forName("org.postgresql.Driver");
                        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/med", "aya", "123");
                        System.out.println("Connected");
                        Statement stmt = con.createStatement();
                        LocalDateTime currentDateTime = getCurrentDateTime();
                        String queryString = "insert into logs values ( '" + currentDateTime + "' , 'ANS1 to CDR' ,'" + asn1FilePath + "' ,'" + cdrFilePath + "' )";
                        stmt.executeUpdate(queryString);
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(Bsmalah.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("ASN.1 content could not be extracted from the ASN.1 format.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveCDRToFile(String cdrContent, String cdrFilePath) {
        try {
            FileWriter fileWriter = new FileWriter(cdrFilePath);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(cdrContent);
            writer.close();
            System.out.println("Translated CDR content saved to: " + cdrFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createCDRFilePath(String asn1FilePath) {
        String directory = new File(asn1FilePath).getParent();
        String fileName = "translated.pem";
        return directory + File.separator + fileName;
    }

    public static String createASN1FilePath(String cdrFilePath) {
        String directory = new File(cdrFilePath).getParent();
        String fileName = "translated.txt";
        return directory + File.separator + fileName;
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
//    _______



    public static int countFilesOnFTPServer(String server, int port, String username, String password, String directory) {
        FTPClient ftp = new FTPClient();
        int numFiles = 0;

        try {
            // Connect to the FTP server
            ftp.connect(server, port);
            ftp.login(username, password);

            // Change working directory
            ftp.changeWorkingDirectory(directory);

            // Get the file list
            String[] files = ftp.listNames();

            // Count the number of files
            if (files != null) {
                numFiles = files.length;
            }

            // Disconnect from the FTP server
            ftp.logout();
            ftp.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numFiles;
    }

    public static String getFileNameFromPath(String fullPath) {
        Path path = Paths.get(fullPath);
        return path.getFileName().toString();
    }

   
    public static int countFilesOnSCPServer(String server, int port, String username, String password, String directory) {
        JSch jsch = new JSch();
        int numFiles = 0;

        try {
            // Connect to the SCP server
            Session session = jsch.getSession(username, server, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Create an SCP channel
            Channel channel = session.openChannel("exec");
            ChannelExec execChannel = (ChannelExec) channel;
            execChannel.setCommand("ls " + directory + " | wc -l");

            // Connect and execute the command
            execChannel.connect();

            // Read the output of the command
            InputStream in = execChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            if ((line = reader.readLine()) != null) {
                numFiles = Integer.parseInt(line.trim());
            }

            // Disconnect from the SCP server
            execChannel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }

        return numFiles;
    }

    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1);
        } else {
            return "";
        }
    }

}