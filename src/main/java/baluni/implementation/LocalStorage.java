package baluni.implementation;

import baluni.filestorage.MyFileStorage;
import baluni.filestorage.StorageConfig;
import baluni.filestorage.StorageManager;
import baluni.implementation.comparators.SortByCreationDate;
import baluni.implementation.comparators.SortByModificationDate;
import baluni.implementation.comparators.SortByName;
import baluni.model.Fajl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class LocalStorage extends MyFileStorage {

    private long usedCapacity = 0;

    static {
        StorageManager.initStorage(new LocalStorage());
    }

    @Override
    public void setStorageConfig(StorageConfig storageConfig) {
        super.setStorageConfig(storageConfig);
    }

    @Override
    public StorageConfig getStorageConfig() {
        return super.getStorageConfig();
    }


    @Override
    public boolean createStorage(String storagePath){
        this.setSotragePath(storagePath);

        if(new File(storagePath + "\\config.json").exists()){
            this.readConfig(this.getSotragePath()+"\\config.json");
            return true;
        }else{
            if(new File(storagePath).exists()){
                StorageConfig storageConfig = new StorageConfig();
                this.setStorageConfig(storageConfig);
                this.saveStorageConfig(this.getSotragePath() + "\\" + "config.json");
                return true;
            }else{
                File pathCreation = new File(storagePath);

                if(pathCreation.mkdir()){
                    this.writeDefaultConfig(this.getSotragePath() + "\\" + "config.json");
                    return true;
                }else{
                    System.out.println("Cannot create path for " + storagePath);
                    return false;
                }
            }
        }
    }

    @Override
    public boolean createDirectories(String destination, String creationPattern) {
        if(destination.isEmpty() || destination.equals("."))
            destination = "";

        if(!(new File(this.getSotragePath() + "\\" + destination).exists() && new File(this.getSotragePath() + "\\" + destination).isDirectory())){
            System.out.println("Directory on this path doesn't exist");
            return false;
        }

        String[] data = new String[100];
        String[] range = new String[100];
        String fileName = "";

        if(creationPattern.contains("{")){
            data = creationPattern.split("\\{");
            if(data[1].contains(".."))
                range = data[1].split("\\.\\.");
            if(data[1].contains("->"))
                range = data[1].split("->");
        }

        if(creationPattern.contains("[")){
            data = creationPattern.split("\\[");
            range = data[1].split(":");
        }

        fileName = data[0];


        int start_idx = -1;
        int end_idx = -1;

        try{
            start_idx = Integer.parseInt(range[0]);
            end_idx = Integer.parseInt(range[1].substring(0, range[1].length()-1));
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

        if(start_idx < 0 || end_idx < 0){
            System.out.println("Please provide positive values for range");
            return false;
        }else{
            System.out.println(start_idx + " " + end_idx);
        }

        if(start_idx > end_idx){
            for(int i=start_idx;i>=end_idx;i--){
                if(this.getStorageConfig().getFoldersWithCapacity().containsKey(Paths.get(this.getSotragePath() + "\\" + destination).toString())){
                    int destContentSize = this.listFilesInDir(destination).size()
                            + this.listDirsForDir(destination).size();
                    int allowdContentSize = this.getStorageConfig().getFoldersWithCapacity().get(Paths.get(this.getSotragePath() + "\\" + destination).toString());
                    if(destContentSize >= allowdContentSize) {
                        System.out.println("Directory is full");
                        return false;
                    }
                }
                File newDir = new File(this.getSotragePath() + "\\" + destination + "\\" + fileName+i);

                if(newDir.exists()){
                    System.out.println(fileName + i + " already exists");
                    continue;
                }

                if(!newDir.mkdir()){
                    System.out.println("Couldn't create directory with name " + fileName+i);
                    return false;
                }
            }
        }else{
            for (int i = start_idx; i <= end_idx; i++) {
                if(this.getStorageConfig().getFoldersWithCapacity().containsKey(Paths.get(this.getSotragePath() + "\\" + destination).toString())){
                    int destDirSize = this.listDirsForDir(destination).size();
                    int destFilesSize = this.listFilesInDir(destination).size();

                    int destContentSize = destDirSize + destFilesSize;

                    int allowdContentSize = this.getStorageConfig().getFoldersWithCapacity().get(Paths.get(this.getSotragePath() + "\\" + destination).toString());
                    if(destContentSize >= allowdContentSize) {
                        System.out.println("Directory is full");
                        return false;
                    }
                }
                File newDir = new File(this.getSotragePath() + "\\" + destination + "\\" + fileName+i);

                if (newDir.exists()) {
                    System.out.println(fileName + i + " already exists");
                    continue;
                }

                if (!newDir.mkdir()) {
                    System.out.println("Couldn't create directory with name " + fileName + i);
                    return false;
                }
            }
        }

        return true;
    }


    @Override
    public boolean createDir(String destination, String dirName) {

        if(destination.isEmpty() || destination.contains("."))
            destination = "";

        File path = new File(this.getSotragePath()+"\\"+destination);

        if(!path.exists()) {
            System.out.println("Destinacija ne postoji");
            return false;
        }

        if(this.getStorageConfig().getFoldersWithCapacity().containsKey(this.getSotragePath()+"\\"+destination)){
            int currSize = this.listFilesInDir(destination).size()
                    + this.listDirsForDir(destination).size();
            int allowedSize = this.getStorageConfig().getFoldersWithCapacity().get(this.getSotragePath() + "\\" +destination);

            if(currSize >= allowedSize){
                System.out.println("Directory full");
                return false;
            }
        }

        String folderPath = path + "\\" + dirName;

        File folder = new File(folderPath);

        if(folder.exists())
            return false;

        return folder.mkdir();
    }


    @Override
    public boolean createDirectory(String path, String folderName, int allowedItems) {
        if(path.isEmpty() || path.contains("."))
            path = "";

        File desiredPath = new File(this.getSotragePath() + "\\" + path);

        if(!desiredPath.exists())
            return false;

        File folder = new File(desiredPath+"\\"+folderName);

        if(folder.exists())
            return false;

        this.getStorageConfig().getFoldersWithCapacity().put(folder.getAbsolutePath(), allowedItems);
        return folder.mkdir();
    }


    @Override
    public boolean createFile(String path, String fileName) {
        if(path.isEmpty() || fileName.isEmpty())
            path = "";

        File f = new File(this.getSotragePath()+"\\"+path);
        String data[] = fileName.split("\\.");

        for(int i=1;i<data.length;i++){
            if(this.getStorageConfig().getForbiddenExtensions().contains("."+data[i])){
                System.out.println("Cannot create file with extension " + data[i] + " because that extenstion is forbidden");
                return false;
            }
        }

        if(!(f.exists()))
            return false;

        if(!(f.isDirectory()))
            return false;

        if(this.getStorageConfig().getFoldersWithCapacity().containsKey(Paths.get(this.getSotragePath() + "\\" + path).toString())){
            int destContentSize = this.listFilesInDir(path).size() + this.listDirsForDir(path).size();
            int allowdContentSize = this.getStorageConfig().getFoldersWithCapacity().get(this.getSotragePath() + "\\" + path);
            if(destContentSize >= allowdContentSize) {
                System.out.println("Directory is full");
                return false;
            }
        }

        boolean windows = f.getPath().contains("\\");

        if(windows){
            File file = new File(this.getSotragePath()+"\\"+path+"\\"+fileName);
            if(file.exists()){
                System.out.println("Ovaj fajl vec posotji");
                return false;
            }
            try{
                boolean creationResult = file.createNewFile();

                if(!creationResult){
                    System.out.println("Ne mogu da napravim fajl " + fileName);
                    return false;
                }
            }catch (IOException exception){
                exception.printStackTrace();
            }
        }else{
            File file = new File(path+"/"+fileName);

            if(file.exists()){
                System.out.println("Ovaj fajl vec postoji");
                return false;
            }

            try{
                boolean creationResult = file.createNewFile();

                if(!creationResult){
                    System.out.println("Ne mogu da kreiram fajl");
                    return false;
                }
                System.out.println("Kreiran fajl");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return true;
    }


    @Override
    public void buildPath(String path) {
        String absPath = this.getSotragePath();
        String[] parsedData = path.split("\\\\");

        for(String parsed : parsedData){
            if(!parsed.contains(".")){
                if(absPath.contains(parsed)){
                    continue;
                }
                File dir = new File(absPath+"\\"+parsed);
                if(dir.mkdir())
                    System.out.println("okej");
                absPath += "\\" + parsed;
            }
        }
    }

    @Override
    public void fileUpload(String s, List<Fajl> list) {
        System.out.println("File upload not supported on local storage");
        return;
    }

    @Override
    public void deleteFiles(List<Fajl> list) {
        for(Fajl fajl : list){
            //Neka pocetna ideja, nekako treba da izbildujem pun path fajla
            String fullPath = fajl.getPath();
            File file = new File(fullPath);

            if(file.exists()) {
                long fileSize = 0;
                try{
                    fileSize = Files.size(Paths.get(fullPath));
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (file.delete()) {
                    usedCapacity -= fileSize;
                    System.out.println(fajl.getFileName() + " je obrisan uspesno");
                } else {
                    System.out.println(fajl.getFileName() + " nije moguce obrisati");
                }
            }else{
                System.out.println("Fajl " + fajl.getFileName() + " ne postoji");
            }
        }
    }

    @Override
    public void deleteDirectories(List<Fajl> list) {
        // java ne moze da obrise direkotrijume koji sadrze fajlove u sebi
        // potrebno je prvo obrisati sadrzaj direktorijuma pa tek onda
        // obrisati sam direktorijum

        for(Fajl fajl : list){
            File dir = new File(fajl.getPath());

            if(dir.exists() && dir.isDirectory()){
                File[] directoryFiles = dir.listFiles();

                this.getStorageConfig().getFoldersWithCapacity().remove(dir.getAbsolutePath());

                if(directoryFiles != null){
                    for(File file : directoryFiles){
                        if(!file.isDirectory()){
                            long fileSize = 0;
                            try{
                                fileSize = Files.size(Paths.get(fajl.getPath()));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if(file.delete()){
                                usedCapacity -= fileSize;
                                System.out.println("Fajl " + file.getName() + " je uspesno obrisan");
                            }else{
                                System.out.println("Fajl " + file.getName() + " nije moguce obrisati");
                            }
                        }else{
                            File[] subFiles = file.listFiles();

                            if(subFiles != null){
                                for(File subFile : subFiles){
                                    long fileSize = 0;
                                    try{
                                        fileSize = Files.size(Paths.get(subFile.getPath()));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if(subFile.delete()){
                                        usedCapacity -= fileSize;
                                        System.out.println("Fajl " + subFile.getName() + " iz poddirektorijuma " + file.getName() + " je uspesno obrisan");
                                    }else{
                                        System.out.println("Fajl " + subFile.getName() + " iz poddirektorijuma " + file.getName() + " nije moguce obrisati");
                                    }
                                }
                            }

                            long fileSize = 0;
                            try{
                                fileSize = Files.size(Paths.get(file.getPath()));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            if(file.delete()){
                                usedCapacity -= fileSize;
                                System.out.println("Direktorijum " + file.getName() + " je uspesno obrisan");
                            }else{
                                System.out.println("Direktorijum " + file.getName() + " nije moguce obrisati");
                            }
                        }
                    }

                    long fileSize = 0;
                    try{
                        fileSize = Files.size(Paths.get(dir.getPath()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if(dir.delete()){
                        usedCapacity -= fileSize;
                        System.out.println("Direktorijum " + dir.getName() + " je uspesno obrisan");
                    }else{
                        System.out.println("Direktorijum " + dir.getName() + " nije moguce obrisati");
                    }
                }
            }
        }
    }

    @Override
    public void moveFiles(String source, String destination) {

        File sourceDir=new File(this.getSotragePath() + "\\" + source);
        File [] sourceDirFiles=sourceDir.listFiles();

        File destinationDir=new File(this.getSotragePath() + "\\" + destination);

        if(!(destinationDir.exists()))
            return;

        if(!(destinationDir.isDirectory()))
            return;

        if(sourceDirFiles == null)
            return;

//        for(File file : sourceDirFiles){
//            System.out.println(file.getPath());
//        }

        for (File file:sourceDirFiles){
            Path result=null;

            String[] data = file.getName().split("\\.");

            if(this.getStorageConfig().getForbiddenExtensions().contains(data[data.length-1]))
                return;

            if(this.getStorageConfig().getFoldersWithCapacity().containsKey(Paths.get(Paths.get(this.getSotragePath() + "\\" + destination).toString()).toString())){
                int destContentSize = this.listFilesInDir(destination).size() + this.listDirsForDir(destination).size();
                int allowdContentSize = this.getStorageConfig().getFoldersWithCapacity().get(Paths.get(this.getSotragePath() + "\\" + destination).toString());
                if(destContentSize >= allowdContentSize) {
                    System.out.println("Directory is full");
                    return;
                }
            }

            long fileSize = 0;

            try {
                fileSize = Files.size(Paths.get(file.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(usedCapacity + fileSize > this.getStorageConfig().getDefaultStorageSize()){
                System.out.println("Storage capacity full");
                return;
            }

            usedCapacity += fileSize;

            try{
                // Hardcode za windows, treba podrzati multi-platform
                // Ideja je da program odredi koji je flavor, ako je windows onda koristi \
                // Ako je unix like sistem koristi /

                result = Files.move(Paths.get(file.getPath()),
                        Paths.get(this.getSotragePath() + "\\" + destination + "\\" + file.getName()),
                        StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                e.printStackTrace();
            }

            if(result==null)
                return;
            System.out.println("pomereni");
        }
    }

    @Override
    public void moveFile(String filePath, String destination) {
        File file = new File(this.getSotragePath() + "\\" + filePath);
        if(!file.isDirectory()){
            if(file.exists()){
                String[] data = file.getName().split("\\.");

                if(this.getStorageConfig().getForbiddenExtensions().contains(data[data.length-1])) {
                    System.out.println("This extension is forbidden");
                    return;
                }

                if(this.getStorageConfig().getFoldersWithCapacity().containsKey(Paths.get(this.getSotragePath() + "\\" + destination).toString())){
                    int destContentSize = this.listFilesInDir(destination).size() + this.listDirsForDir(destination).size();
                    int allowdContentSize = this.getStorageConfig().getFoldersWithCapacity().get(this.getSotragePath() + "\\" + destination);
                    if(destContentSize >= allowdContentSize) {
                        System.out.println("Directory is full");
                        return;
                    }
                }


                long fileSize = 0;

                try {
                    fileSize = Files.size(Paths.get(file.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(usedCapacity + fileSize > this.getStorageConfig().getDefaultStorageSize()){
                    System.out.println("Storage capacity full");
                    return;
                }

                usedCapacity += fileSize;

                try{
                    Files.move(Paths.get(file.getPath()), Paths.get(this.getSotragePath()+ "\\" + destination + "\\" + file.getName()),
                            StandardCopyOption.REPLACE_EXISTING);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void download(String sourcePath, String destPath) {
        File source = new File(this.getSotragePath() + "\\" + sourcePath);
        File dst = new File(destPath);

        if(!source.exists()){
            System.out.println("Ne postoji");
            return;
        }

        if(!dst.exists())
            return;

        if(destPath.contains(this.getSotragePath())){
            System.out.println("Destinacija mora biti izvan storage-a");
            return;
        }

        try {
            if(source.isDirectory()) {
                File newDest = new File(destPath + "\\" + source.getPath().substring(source.getPath().lastIndexOf("\\")));
                System.out.println("a->" + newDest.getPath());
                copyDirectoryCompatibityMode(source, newDest);
            } else {
                File fileDest = new File(destPath + "\\" + source.getPath().substring(source.getPath().lastIndexOf("\\"), source.getPath().length()));
                copyFile(source, fileDest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }
        for (String f : sourceDirectory.list()) {
            System.out.println(new File(destinationDirectory, f).getPath());
            copyDirectoryCompatibityMode(new File(sourceDirectory, f), new File(destinationDirectory, f));
        }
    }

    private void copyDirectoryCompatibityMode(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }

    private void copyFile(File sourceFile, File destinationFile)
            throws IOException {
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    @Override
    public boolean rename(String oldFileName, String newFileName) {
        if(oldFileName.equalsIgnoreCase(newFileName)){
            System.out.println("Novo ime je isto kao i staro");
            return false;
        }

        String data[] = newFileName.split("\\.");

        for(int i=1;i<data.length;i++){
            if(this.getStorageConfig().getForbiddenExtensions().contains(data[i])){
                System.out.println("Cannot rename to the name containing forbidden extension");
                return false;
            }
        }

        Path source = Paths.get(this.getSotragePath() + "\\" + oldFileName);
        try {
            Files.move(source, source.resolveSibling(newFileName));
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<Fajl> listDirsForDir(String path){

        List<Fajl> result = new ArrayList<>();
        File[] files;

        if(path.isEmpty() || path.equals("."))
            path = "";

        File src = new File(this.getSotragePath()+"\\"+path);

        if(!src.isDirectory()){
            return result;
        }

        files = src.listFiles();

        if(files == null)
            return result;

        for(File file : files){
            if(!file.isDirectory())
                continue;

            Path tempFile = file.getAbsoluteFile().toPath();

            try{
                BasicFileAttributes attr = Files.readAttributes(tempFile, BasicFileAttributes.class);

                LocalDate creationTime = LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                LocalDate modificationTime = LocalDate.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                long fileSize = attr.size();

                String fileName = file.getName();
                String extension = "dir";

                result.add(new Fajl(fileName,extension,tempFile.toString(),creationTime,modificationTime,fileSize));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public List<Fajl> listFilesInDir(String dirPath) {
        if(dirPath.isEmpty() || dirPath.contains("."))
            dirPath = "";

        File directory = new File(this.getSotragePath() + "\\" + dirPath);

        List<Fajl> results = new ArrayList<>();

        if(!(directory.exists() || directory.isDirectory())){
            return results;
        }

        File[] listOfFiles = directory.listFiles();

        if(listOfFiles == null)
            return results;

        for(File file : listOfFiles){
            if(file.isDirectory())
                continue;

            Path tempFile = file.getAbsoluteFile().toPath();
            try {
                BasicFileAttributes attr = Files.readAttributes(tempFile, BasicFileAttributes.class);
                // Mozda preuredjivanje?

                String fileName = "";
                String extension = "";

                if (file.getName().contains(".")){
                    String[] data = file.getName().split("\\.");
                    fileName = data[0];
                    extension = data[data.length - 1];
                }else {
                    fileName = file.getName();
                    extension = "";
                }

                results.add(new Fajl(fileName,
                        extension,
                        tempFile.toString(),
                        LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault()),
                        LocalDate.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault()),attr.size()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    @Override
    public List<Fajl> listFilesInSubDir(String dirPath) {
        System.out.println(this.getSotragePath());
        List<Fajl> result = new ArrayList<>(this.listFilesInDir(dirPath));
        List<Fajl> dirs = new ArrayList<>(this.listDirsForDir(dirPath));

        while(!dirs.isEmpty()){
            Fajl dir = dirs.get(0);
            String tempPath = dir.getPath().substring(this.getSotragePath().length()+1,dir.getPath().length());
            dirs.addAll(this.listDirsForDir(tempPath));
            result.addAll(this.listFilesInDir(tempPath));
            dirs.remove(0);
        }

        return result;
    }

    @Override
    public List<Fajl> listFiles(String dirPath) {
        return listFilesInDir(dirPath);
    }

    @Override
    public List<Fajl> listFilesForExtension(String extension) {
        List<Fajl> storageContent = listFiles("");
        List<Fajl> results = new ArrayList<>();

        List<String> forbiddenExt=this.getStorageConfig().getForbiddenExtensions();

        if(forbiddenExt.contains(extension)) {
            System.out.println("Handler za ekstenzije");
            return results;
        }

        for(Fajl file : storageContent){
            if(file.getExtension().equalsIgnoreCase(extension))
                results.add(file);
        }

        return results;
    }

    @Override
    public List<Fajl> listFilesForExtension(String dir, String extension) {

        if(dir.isEmpty() || dir.equals("."))
            dir = "";

        File src = new File(this.getSotragePath() + "\\" + dir);

        List<Fajl> files = new ArrayList<>();
        List<Fajl> result = new ArrayList<>();

        if(!src.isDirectory())
            return files;

        files = this.listFilesInDir(dir);

        for(Fajl fajl : files){
            if(fajl.getExtension().equals(extension)){
                result.add(fajl);
            }
        }

        return result;
    }

    @Override
    public List<Fajl> listFilesForName(String name) {
        List<Fajl> storageContent = listFiles("");
        List<Fajl> results=new ArrayList<>();

        if(name.equals(""))
            return null;

        for(Fajl file:storageContent){
            if(file.getFileName().contains(name))
                results.add(file);
        }

        return results;
    }

    @Override
    public List<Fajl> listFilesForName(String dir, String fileName) {
        File src = new File(this.getSotragePath()+"\\"+dir);

        List<Fajl> files = new ArrayList<>();
        List<Fajl> result = new ArrayList<>();

        if(!src.isDirectory())
            return files;

        files = this.listFilesInDir(dir);

        System.out.println(files.size());

        for(Fajl fajl : files){
            if(fajl.getFileName().contains(fileName)){
                result.add(fajl);
            }
        }

        return result;
    }

    @Override
    public boolean listDirForNames(String dirPath, List<String> list) {
        if(dirPath.isEmpty() || dirPath.equals(".")){
            dirPath = "";
        }

        List<Fajl> dirContent = listFilesInDir(dirPath);
        List<String> fileNames = new ArrayList<>();

        for(Fajl file:dirContent) {
            fileNames.add(file.getFileName());
        }
        if(list.isEmpty())
            return false;

        if(fileNames.containsAll(list))
            return true;
        return false;
    }

    private boolean findFileInDir(String dirPath, String fileName){
        if(dirPath.isEmpty() || dirPath.equals("."))
            dirPath = "";

        File dir = new File(this.getSotragePath()+"\\"+dirPath);

        if(!(dir.exists() || dir.isDirectory())){
            return false;
        }

        File[] dirFiles = dir.listFiles();

        if(dirFiles == null)
            return false;

        for(File currFile : dirFiles){
            if(currFile.getName().equals(fileName)){
                return true;
            }
        }

        return false;
    }

    @Override
    public Fajl findDirectoryOfFile(String fileName) {
        String storage = this.getSotragePath();

        File currDir = new File(storage);

        File[] storageFiles = currDir.listFiles();

        if(storageFiles == null)
            return null;

        File result = null;

        boolean found = false;

        for(File currFile : storageFiles){
            if(currFile.getName().equals(fileName)) {
                result = currFile;
                found = true;
            }
        }

        Fajl rezultat = null;

        try {
            BasicFileAttributes attributes =  Files.readAttributes(currDir.toPath(), BasicFileAttributes.class);

            String name = currDir.getName();
            LocalDate dateCreated = LocalDate.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());
            LocalDate dateModified = LocalDate.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            String ext = "dir";
            long size = 0;

            if(found)
                return new Fajl(name, ext, currDir.getAbsolutePath(), dateCreated, dateModified, size);

            for(File currFile : storageFiles){
                boolean flag = false;
                if(currFile.isDirectory()){
                    flag = findFileInDir(currFile.getName(), fileName);
                    if(flag) {
                        result = currFile;
                        break;
                    }
                }
            }

            if(result == null)
                return null;

            name = result.getName();
            dateCreated = LocalDate.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());
            dateModified = LocalDate.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            ext = "dir";
            size = 0;

            rezultat = new Fajl(name, ext, result.getAbsolutePath(), dateCreated, dateModified, size);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rezultat;
    }

    //Videti sort za fajlove numericke i alfanumericke - nije najprirodnije
    @Override
    public List<Fajl> sort(List<Fajl> fileList,  boolean byName, boolean creationDate, boolean dateModified, boolean asc) {
        if(byName){
            fileList.sort(new SortByName(asc));
        }

        if(creationDate){
            fileList.sort(new SortByCreationDate(asc));
        }

        if(dateModified){
            fileList.sort(new SortByModificationDate(asc));
        }

        return fileList;
    }

    @Override
    public List<Fajl> listFileByDate(String date, String dirPath) {

        LocalDate datum = LocalDate.parse(date);

        List<Fajl> resultList = new ArrayList<Fajl>();

        if(dirPath.isEmpty() || dirPath.equals("."))
            dirPath = this.getSotragePath();

        File directory = new File(dirPath);

        if(!directory.exists())
            return null;

        if(!directory.isDirectory())
            return null;

        File[] dirFiles = directory.listFiles();

        for(File file : dirFiles){
            if(file.isDirectory())
                continue;
            Path filePath = file.getAbsoluteFile().toPath();
            Fajl tempFajl = new Fajl("","","");

            String data[] = new String[100];

            try {
                BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);

                LocalDate fileCreationDate = LocalDate.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());
                LocalDate fileModificationDate = LocalDate.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault());

                if(datum.equals(fileModificationDate) || datum.equals(fileCreationDate)){
                    if(file.getName().contains("."))
                        data = file.getName().split("\\.");
                    else {
                        data[0] = file.getName();
                        data[1] = "";
                    }

                    tempFajl.setFileName(data[0]);
                    tempFajl.setExtension(data[1]);
                    tempFajl.setCreationDate(fileCreationDate);
                    tempFajl.setModificationDate(fileModificationDate);
                    if(file.isDirectory())
                        tempFajl.setFileSize(0);
                    else
                        tempFajl.setFileSize(attributes.size());
                    tempFajl.setPath(filePath.toString());

                    resultList.add(tempFajl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return resultList;
    }

    @Override
    public List<Fajl> listFilesBetweenDates(String startDate, String endDate, String dirPath) {
        List<Fajl> resultSet=new ArrayList<>();

        LocalDate localStart = LocalDate.parse(startDate);
        LocalDate localEnd = LocalDate.parse(endDate);

        if(dirPath.isEmpty() || dirPath.equals("."))
            dirPath = this.getSotragePath();

        File dir=new File(dirPath);

        if(!(dir.exists()))
            return null;

        if(!(dir.isDirectory()))
            return null;


        File[] files=dir.listFiles();
        for(File file:files){
            if(file.isDirectory())
                continue;
            Path filePath = file.getAbsoluteFile().toPath();
            Fajl tempFajl=new Fajl("","", "");

            String data[] = new String[100];

            try {
                BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                LocalDate creationDate=LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                LocalDate modificationDate=LocalDate.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());

                if(creationDate.isBefore(localEnd) && creationDate.isAfter(localStart )
                        || (modificationDate.isAfter(localStart) && modificationDate.isBefore(localEnd))
                        || ((modificationDate.equals(localEnd) || (modificationDate.equals(localStart))
                        || ((creationDate.equals(localEnd) || (creationDate.equals(localStart))))))){

                    if(file.getName().contains("."))
                        data = file.getName().split("\\.");
                    else {
                        data[0] = file.getName();
                        data[1] = "";
                    }

                    tempFajl.setFileName(data[0]);
                    tempFajl.setExtension(data[1]);
                    tempFajl.setCreationDate(creationDate);
                    tempFajl.setModificationDate(modificationDate);
                    if(file.isDirectory())
                        tempFajl.setFileSize(0);
                    else
                        tempFajl.setFileSize(attr.size());
                    tempFajl.setPath(filePath.toString());

                    resultSet.add(tempFajl);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return resultSet;
    }

    @Override
    public List<Fajl> filterData(List<Fajl> fileList,boolean byPath,boolean byName,boolean bySize,boolean byCreationDate
            ,boolean byModificationDate, boolean byExtension) {

        List<Fajl> outputList = new ArrayList<>();

        // test1 txt /root/desktop 1020.1420240. 4102401.41204 2048

        // Kako da ogranicimo da ne dodajemo u listu kada je file null

        for(Fajl fajl : fileList){
            Fajl newFile = new Fajl("","","",null,null,0);

            if(byPath){
                newFile.setPath(fajl.getPath());
            }

            if(byName){
                newFile.setFileName(fajl.getFileName());
            }

            if(bySize){
                newFile.setFileSize(fajl.getFileSize());
            }

            if(byCreationDate){
                newFile.setCreationDate(fajl.getCreationDate());
            }

            if(byModificationDate){
                newFile.setModificationDate(fajl.getModificationDate());
            }

            if(byExtension){
                newFile.setExtension(fajl.getExtension());
            }

            outputList.add(newFile);
        }

        return outputList;
    }

}
