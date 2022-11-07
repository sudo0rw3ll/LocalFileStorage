package baluni.implementation;

import baluni.filestorage.MyFileStorage;
import baluni.filestorage.StorageConfig;
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

    static List<Fajl> listaFajlova = new ArrayList<>();

    @Override
    public void setStorageConfig(StorageConfig storageConfig) {
        super.setStorageConfig(storageConfig);
    }

    @Override
    public StorageConfig getStorageConfig() {
        return super.getStorageConfig();
    }

    @Override
    public boolean createStorage(String storagePath) {
        Scanner scanner = new Scanner(System.in);
        File file = new File(storagePath);
        //check if storage already exists on given path
        if(file.isDirectory() && file.exists()){
            // System.out.println("Storage with this name already exists, try using another name.");
            File[] dirFiles = file.listFiles();

            boolean is_storage = false;

            for(File dirFile : dirFiles){
                if(dirFile.getName().equals("storage_config.json")){
                    is_storage = true;
                }
            }

            if(!is_storage){
                System.out.println("Folder with this name already exists, try using another name");
                return false;
            }else{
                System.out.println("This folder exists and it's already a storage, do you want to use it [yes/no]? ");
                String choice = scanner.nextLine();

                if(choice.equalsIgnoreCase("yes")){
                    this.setSotragePath(storagePath);
                    return true;
                }else{
                    System.out.println("Okay please provide new path for storage\nExiting...");
                    return false;
                }
            }
//            return false;
        }

        boolean fwdSlash = false;
        File config = null;

        if(!storagePath.startsWith("C:\\")){
            fwdSlash = true;
        }

        if(file.mkdir()){
            System.out.println("Do you want to specify the path of config file [yes/no]? ");

            String choice = scanner.nextLine();

            if(choice.equalsIgnoreCase("no")){
                System.out.println("Okay, default config will be created");
                if(!fwdSlash){
                    System.out.println(storagePath + "\\" + "default_config.json");
                    try {
                        config = new File(storagePath + "\\" + "default_config.json");
                        config.createNewFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else {
                    try {
                        config = new File(storagePath + "/" + "default_config.json");
                        config.createNewFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            if(choice.equalsIgnoreCase("yes")) {
                System.out.println("Enter storage config path: ");
                String configFilePath = scanner.nextLine();

                if(configFilePath.isEmpty()){
                    System.out.println("Path of config file empty, creating default config");

                    if(!fwdSlash) {
                        try {
                            config = new File(storagePath + "\\" + "default_config.json");
                            config.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            config = new File(storagePath + "/" + "default_config.json");
                            config.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    System.out.println("Creating specific config");
                    // ne bi bilo lose napraviti funkciju, moveFile(String filePath, String destDirPath);
                    // pomera jedan fajl sa neke lokacije u neki direktorijum koji se prosledi
                    // moveFiles radi tako sto kontent jednog direktorijuma prebaci u drugi
                    moveFiles(configFilePath, storagePath);
                }
            }

            System.out.println("Local storage has been created successfully!");
            this.setSotragePath(storagePath);
            return true;
        }

        return false;

    }

    @Override
    public boolean createDirectory(String destination, String creationPattern) {
        //mkdir C:\\Users\\Vid\\Desktop\\s{1..20}
        //mkdir C:\\Users\\Vid\\Desktop\\s[1:20]
        //mkdir C:\\Users\\Vid\\Desktop\\s{20->10}

        if(!(new File(destination).exists() && new File(destination).isDirectory())){
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
        
//        String[] data = creationPattern.split("\\{");
//
//        String fileName = data[0];
//
//        String[] range = data[1].split("\\.\\.");

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

        // Treba sredjivanje koda, ima RY

        if(start_idx > end_idx){
            for(int i=start_idx;i>=end_idx;i--){
                File newDir = new File(destination+"\\"+fileName+i);

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
                File newDir = new File(destination + "\\" + fileName + i);

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
    public boolean createFile(String path, String fileName) {
        if(path.isEmpty() || fileName.isEmpty())
            return false;

        File f = new File(path);

        if(!(f.exists()))
            return false;

        if(!(f.isDirectory()))
            return false;

        // Proveriti da li treba provera ekstenzije
//        //slika.jpg
//        if(!fileName.contains("."))
//            return false;

        boolean windows = path.contains("\\");

        if(windows){
            File file = new File(path+"\\"+fileName);
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
                //Kreiranje objekta tipa Fajl
                //joka.txt
                String[] data = fileName.split("\\.");

                Fajl fajl = new Fajl(data[0],data[1],path, LocalDate.now(), LocalDate.now(),0);

                if(!listaFajlova.contains(fajl)) {
                    listaFajlova.add(fajl);
                    System.out.println(fajl);
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

    //Windows only
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

    }

    @Override
    public void deleteFiles(List<Fajl> list) {
        for(Fajl fajl : list){
            //Neka pocetna ideja, nekako treba da izbildujem pun path fajla
            String fullPath = fajl.getPath() + fajl.getFileName() + fajl.getExtension();
            File file = new File(fullPath);

            if(file.exists()) {
                if (file.delete()) {
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

                if(directoryFiles != null){
                    for(File file : directoryFiles){

                        if(!file.isDirectory()){
                            if(file.delete()){
                                System.out.println("Fajl " + file.getName() + " je uspesno obrisan");
                            }else{
                                System.out.println("Fajl " + file.getName() + " nije moguce obrisati");
                            }
                        }else{
                            File[] subFiles = file.listFiles();

                            if(subFiles != null){
                                for(File subFile : subFiles){
                                    if(subFile.delete()){
                                        System.out.println("Fajl " + subFile.getName() + " iz poddirektorijuma " + file.getName() + " je uspesno obrisan");
                                    }else{
                                        System.out.println("Fajl " + subFile.getName() + " iz poddirektorijuma " + file.getName() + " nije moguce obrisati");
                                    }
                                }
                            }

                            if(file.delete()){
                                System.out.println("Direktorijum " + file.getName() + " je uspesno obrisan");
                            }else{
                                System.out.println("Direktorijum " + file.getName() + " nije moguce obrisati");
                            }
                        }
                    }

                    if(dir.delete()){
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

        File sourceDir=new File(source);
        File [] sourceDirFiles=sourceDir.listFiles();

        File destinationDir=new File(destination);

        if(!(destinationDir.exists()))
            return;

        if(!(destinationDir.isDirectory()))
            return;

        if(sourceDirFiles == null)
            return;

        for(File file : sourceDirFiles){
            System.out.println(file.getPath());
        }

        for (File file:sourceDirFiles){
            Path result=null;
            try{
                // Hardcode za windows, treba podrzati multi-platform
                // Ideja je da program odredi koji je flavor, ako je windows onda koristi \
                // Ako je unix like sistem koristi /

                result = Files.move(Paths.get(file.getPath()),
                        Paths.get(destination+"\\"+file.getName()),
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
    public void download(String sourcePath, String destPath) {
        File source = new File(sourcePath);
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
                File newDest = new File(destPath + "\\" + sourcePath.substring(sourcePath.lastIndexOf("\\")));
                System.out.println("a->" + newDest.getPath());
                copyDirectoryCompatibityMode(source, newDest);
            } else {
                File fileDest = new File(destPath + "\\" + sourcePath.substring(sourcePath.lastIndexOf("\\"), sourcePath.length()));
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

    // TREBA DODATI ERROR HANDLING A NE OVAJ SELJACKI
    @Override
    public boolean rename(String oldFileName, String newFileName) {
        if(oldFileName.equalsIgnoreCase(newFileName)){
            System.out.println("Novo ime je isto kao i staro");
            return false;
        }

        Path source = Paths.get(oldFileName);
        try {
            Files.move(source, source.resolveSibling(newFileName));
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public List<Fajl> listFilesInDir(String dirPath) {
        File directory = new File(dirPath);

        List<Fajl> results = new ArrayList<>();

        if(!(directory.exists() || directory.isDirectory())){
            return null;
        }

        File[] listOfFiles = directory.listFiles();

        if(listOfFiles == null)
            return null;

        for(File file : listOfFiles){
            Path tempFile = file.getAbsoluteFile().toPath();
            try {
                BasicFileAttributes attr = Files.readAttributes(tempFile, BasicFileAttributes.class);
                String[] data = new String[100];

                // Mozda preuredjivanje?

                if(file.getName().contains("."))
                    data = file.getName().split("\\.");
                else {
                    data[0] = file.getName();
                    data[1] = "";
                }

                Fajl fajl = new Fajl(data[0],
                        "."+data[1],
                        tempFile.toString(),
                        LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault()),
                        LocalDate.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault()),attr.size());

                if(!results.contains(fajl))
                    results.add(fajl);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    @Override
    public List<Fajl> listFilesInSubDir(String dirPath) {
        File directory = new File(dirPath);

        List<Fajl> result = new ArrayList<>();

        if(!(directory.exists() || directory.isDirectory())){
            return null;
        }

        File[] contentsOfDir = directory.listFiles();

        if(contentsOfDir == null)
            return null;

        for(File file : contentsOfDir){
            if(file.isDirectory()){
                result.addAll(listFilesInDir(file.getPath()));
            }
        }

        return result;
    }

    @Override
    public List<Fajl> listFiles(String dirPath) {
        List<Fajl> results = new ArrayList<Fajl>();

        results.addAll(listFilesInDir(dirPath));
        results.addAll(listFilesInSubDir(dirPath));

        return results;
    }

    @Override
    public List<Fajl> listFilesForExtension(String extension) {
        List<Fajl> storageContent = listFiles(getSotragePath());
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
    public List<Fajl> listFilesForName(String name) {
        List<Fajl> storageContent = listFiles(getSotragePath());
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
    public boolean listDirForNames(String dirPath, List<String> list) {
        // C:\\Desktop\\Vid\\joka\\ + testni
        // C:\\Desktop\\Vid\\joka\\testni\\
        // listDirForNames("testni",)
        List<Fajl> dirContent = listFilesInDir(getSotragePath() + dirPath);
        List<String> fileNames = new ArrayList<>();

        for(Fajl file:dirContent)
            fileNames.add(file.getFileName());

        if(list.isEmpty())
            return false;

        if(fileNames.containsAll(list))
            return true;
        return false;
    }

    private boolean findFileInDir(String dirPath, String fileName){
        File dir = new File(dirPath);

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

        if(found)
            return new Fajl(currDir.getName(), "", "");

        for(File currFile : storageFiles){
            boolean flag = false;
            if(currFile.isDirectory()){
                flag = findFileInDir(currFile.getPath(), fileName);
                if(flag) {
                    result = currFile;
                    break;
                }
            }
        }

        if(result == null)
            return null;

        return new Fajl(result.getName(), "", "");
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

        if(dirPath.isEmpty())
            return null;

        File directory = new File(dirPath);

        if(!directory.exists())
            return null;

        if(!directory.isDirectory())
            return null;

        File[] dirFiles = directory.listFiles();

        for(File file : dirFiles){
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
                    tempFajl.setFileSize(0);
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

        if(dirPath.isEmpty())
            return null;

        File dir=new File(dirPath);

        if(!(dir.exists()))
            return null;

        if(!(dir.isDirectory()))
            return null;


        File[] files=dir.listFiles();
        for(File dirFile:files){
            Path filePath = dirFile.getAbsoluteFile().toPath();
            Fajl tmpFajl=new Fajl("","", "");

            String data[] = new String[100];

            try {
                BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                LocalDate creationDate=LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                LocalDate modificationDate=LocalDate.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());

                if(creationDate.isBefore(localEnd) && creationDate.isAfter(localStart )
                        || (modificationDate.isAfter(localStart) && modificationDate.isBefore(localEnd))
                        || ((modificationDate.equals(localEnd) || (modificationDate.equals(localStart))
                        || ((creationDate.equals(localEnd) || (creationDate.equals(localStart))))))){
                    if(dirFile.getName().contains("."))
                        data = dirFile.getName().split("\\.");
                    else {
                        data[0] = dirFile.getName();
                        data[1] = "";
                    }
                    tmpFajl.setPath(filePath.toString());
                    tmpFajl.setFileName(data[0]);
                    tmpFajl.setExtension(data[1]);
                    tmpFajl.setCreationDate(creationDate);
                    tmpFajl.setModificationDate(modificationDate);
                    tmpFajl.setFileSize(0);
                    resultSet.add(tmpFajl);

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
