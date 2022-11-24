package baluni.implementation;

import baluni.filestorage.StorageConfig;
import baluni.model.Fajl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LocalStorage local = new LocalStorage();

        local.createStorage("C:\\Users\\Vid\\Desktop\\vidra_joksim");

        String line = "";

        do{
            Scanner scanner = new Scanner(System.in);

            System.out.printf("# ");
            line = scanner.nextLine();

            switch (line){
                case "setSize":
                    long storageSize = scanner.nextLong();
                    local.getStorageConfig().setDefaultStorageSize(storageSize);
                    break;
                case "setName":
                    String name = scanner.nextLine();
                    local.getStorageConfig().setStorageName(name);
                    break;
                case "setExtensions":
                    String extensions = scanner.nextLine();
                    String[] data = extensions.split(",");

                    for(String d : data) {
//                        local.getStorageConfig().getForbiddenExtensions().add(d);
                        if(!local.getStorageConfig().getForbiddenExtensions().contains(d))
                            local.getStorageConfig().getForbiddenExtensions().add(d);
                    }

                    break;
                case "config":
                    local.saveStorageConfig(local.getSotragePath() + "\\" + "config.json");
                    break;
                case "exit":
                    System.out.println("Bad command");
                    break;
            }
        }while(!line.equalsIgnoreCase("exit"));
//        local.createDirectories("","f{1..4}");
//        local.createDir("s1","novis3");
//        local.createDirectory("","ogranicen3",10);
//        local.createDirectory("","ogranicen4",2);
//        local.createFile("ogranicen4","joka_vidra.txt");
//        local.createFile("ogranicen4","vidra1.txt");
//        local.createFile("ogranicen4","vidra2.txt");
//        local.createDirectories("ogranicen3","test{1..12}");
//        local.moveFiles("s1","ogranicen");
//        local.rename("s3","vidra_joksim");
//        local.download("vidra_joksim","C:\\Users\\Vid\\Desktop\\");
//
//        for(Fajl fajl : local.listDirsForDir(""))
//            System.out.println(fajl);
//
//        System.out.println("=============================");
//
//        for(Fajl fajl : local.listFilesInDir("ogranicen"))
//            System.out.println(fajl);
//
//        System.out.println("=============================");
//
//        for(Fajl fajl : local.listFilesInSubDir(""))
//            System.out.println(fajl);
//
//        System.out.println("Extensions ==================");
//
//        for(Fajl fajl : local.listFilesForExtension("ogranicen","txt"))
//            System.out.println(fajl);
//
//        System.out.println("For Name ==================");
//
//        for(Fajl fajl : local.listFilesForName("ogranicen","test"))
//            System.out.println(fajl);
//
//        System.out.println("Sort ================");
//
//        for(Fajl fajl : local.sort(local.listFilesInDir("ogranicen"),true,false,false,false))
//            System.out.println(fajl);
//
//        System.out.println("Filter ==============");
//
//        for(Fajl fajl : local.filterData(local.listFilesInDir("ogranicen"),true,false,false,false,false,false))
//            System.out.println(fajl);
//
//        local.deleteDirectories(local.listDirsForDir(""));
//
//        local.saveStorageConfig(local.getSotragePath()+"\\config.json");
//





        //        local.createStorage("C:\\Users\\Vid\\Desktop\\stor");
//        System.out.println(local.getStorageConfig().getForbiddenExtensions());
//        local.getStorageConfig().getFoldersWithCapacity().put("C:\\Users\\Vid\\Desktop\\niko", 2);
//        boolean ok = local.createDirectory("C:\\Users\\Vid\\Desktop","niko",2);
//        System.out.println(ok);
//        local.createDirectories("C:\\Users\\Vid\\Desktop\\niko","s{1..20}");

//        local.moveFiles("1lB0kEzeVmQCJ43Wmgn9eqp9KXYvS2B7u","1ndAkrAGIDTQyWkc_3nkpzUIs9MbGfX7S");
//        local.createDir("C:\\Users\\Vid\\Desktop\\niko", "nikola");

//        System.out.println(local.getStorageConfig().getFoldersWithCapacity().get("C:\\Users\\Vid\\Desktop\\niko"));
//        System.out.println(local.getStorageConfig().getFoldersWithCapacity().keySet());
//        local.moveFiles("C:\\Users\\Vid\\Desktop\\tt","C:\\Users\\Vid\\Desktop\\niko");
//
//        local.moveFile("C:\\Users\\Vid\\Desktop\\tt\\a.txt","C:\\Users\\Vid\\Desktop\\niko");
//        local.moveFile("C:\\Users\\Vid\\Desktop\\tt\\b.txt", "C:\\Users\\Vid\\Desktop\\niko");
//        local.moveFile("C:\\Users\\Vid\\Desktop\\tt\\c.txt", "C:\\Users\\Vid\\Desktop\\niko");
//
//        local.getStorageConfig().getFoldersWithCapacity().put("C:\\Users\\Vid\\Desktop\\niko",2);
//        System.out.println(local.listFilesInDir("C:\\Users\\Vid\\Desktop\\niko").size());
//        System.out.println(local.getStorageConfig().getFoldersWithCapacity().get("C:\\Users\\Vid\\Desktop\\niko"));

//        local.setSotragePath("C:\\Users\\Vid\\Desktop\\tt");
//        StorageConfig config = new StorageConfig();
//        config.getForbiddenExtensions().add("exe");
//        config.getForbiddenExtensions().add("pdf");
//        config.getFoldersWithCapacity().put("C:\\Users\\Vid\\Desktop\\tt\\joka\\kapacitet",4);
//        local.setStorageConfig(config);
////        local.createFile1("joka\\kapacitet","test123.txt");
//        for(Fajl fajl : local.listFilesForName("","Dijkstra"))
//            System.out.println(fajl);

//        local.createStorage("C:\\Users\\Vid\\Desktop\\novi_storage");


//        local.listDirsForDir("ogranicen");
//        local.createDirectories("","test{1..4}");
//        local.createDirectory("","ogranicen",3);
//        local.createFile("test1","testKreiranja.txt");
//        local.createFile("test2","testDrugogKreiranja.txt");
//        local.createDirectories("ogranicen","capacity{1..2}");
//        local.createFile("ogranicen","tryMe.exe");
//        local.createDirectories("ogranicen","nekiNaziv{1..3}");
//        local.saveStorageConfig(local.getSotragePath()+"\\"+"config.json");
//        for(Fajl fajl : local.listFiles(""))
//            System.out.println(fajl);
//        List<Fajl> fajlovi = local.listFiles("a");
//
//        for(Fajl fajl : fajlovi)
//            System.out.println(fajl);
//
//        List<Fajl> dir = local.listDirsForDir("");
//
//        local.deleteDirectories(dir);

//        local.moveFiles("jedan","novi");
//        local.download("novi","C:\\Users\\Vid\\Desktop\\");
//        System.out.println(local.listFilesForName("","c"));
//        local.createDire("","testRelative");
        //        local.createDirectorie("joka","s{20..25}");

//        for(Fajl fajl :local.listFilesBetweenDates("2022-11-09","2022-11-20","C:\\Users\\Vid\\Desktop\\tt")){
//            System.out.println(fajl);
//        }
//
//
//
//
//        local.moveFile("C:\\Users\\Vid\\Desktop\\tt\\a.txt","C:\\Users\\Vid\\Desktop\\niko");
//        local.moveFile("C:\\Users\\Vid\\Desktop\\tt\\b.txt","C:\\Users\\Vid\\Desktop\\niko");
//        local.moveFile("C:\\Users\\Vid\\Desktop\\tt\\c.txt","C:\\Users\\Vid\\Desktop\\niko");
//        local.moveFiles("C:\\Users\\Vid\\Desktop\\tt","C:\\Users\\Vid\\Desktop\\niko");
//        StorageConfig storageConfig = new StorageConfig();
////        local.readConfig("C:\\Users\\Vid\\Desktop\\tt\\config.json");
//        local.createStorage("C:\\Users\\Vid\\Desktop\\stor\\");
//        System.out.println(local.getStorageConfig().getStorageName());
//        System.out.println(local.getStorageConfig().getForbiddenExtensions());
//        local.createFile("C:\\Users\\Vid\\Desktop\\stor\\","tryme.jpg");
//        local.createStorage("C:\\Users\\Vid\\Desktop\\joka");
//        local.createDirectory("C:\\Users\\Vid\\Desktop\\joka\\","joka{1..20}");
//        boolean result = local.createFile("C:\\Users\\Vid\\Desktop\\joka\\","joka.txt");
//        System.out.println(result);

//        List<Fajl> fajlovi = new ArrayList<>();
//
//        fajlovi.add(new Fajl("glupost1",".txt","C:\\Users\\Vid\\Desktop\\joka\\", LocalDate.now(), LocalDate.now(),0));
//        fajlovi.add(new Fajl("glupost2",".txt","C:\\Users\\Vid\\Desktop\\joka\\", LocalDate.now(), LocalDate.now(),0));
//        fajlovi.add(new Fajl("glupost3",".txt","C:\\Users\\Vid\\Desktop\\joka\\", LocalDate.now(), LocalDate.now(),0));
//
//        local.deleteFiles(fajlovi);
//        List<Fajl> fajlovi = new ArrayList<>();
//        fajlovi.add(new Fajl("testniDirektorijum","", "C:\\Users\\Vid\\Desktop\\testniDirektorijum",LocalDate.now(), LocalDate.now(), 0));
//        local.deleteDirectories(fajlovi);

//        List<Fajl> result = local.listFiles("C:\\Users\\Vid\\Desktop\\test");
//
//        for(Fajl fajl : result){
//            System.out.println(fajl.toString());
//        }

//        List<String> forbidden = new ArrayList<>();
//        forbidden.add(".jpg");
//        forbidden.add(".png");
//        forbidden.add(".svg");
//        StorageConfig storageConfig = new StorageConfig("Moj storage", 4096,10, forbidden);
//        local.setStorageConfig(storageConfig);
//        local.setSotragePath("C:\\Users\\Vid\\Desktop\\test\\");
//
//        local.setSotragePath("C:\\Users\\Vid\\Desktop\\joka\\");
//        local.download("C:\\Users\\Vid\\Desktop\\joka\\joka20","C:\\Users\\Vid\\Desktop\\tt\\");

//        local.createDirectory("C:\\Users\\Vid\\Desktop\\storka\\","joka{20->1}");
//
//        for(Fajl fajl : local.listFilesForExtension(".svg")){
//            System.out.println(fajl.getFileName() + fajl.getExtension());
//        }

//        System.out.println(local.getSotragePath());
//
//        for(Fajl fajl:local.listFilesForName("C")){
//            System.out.println(fajl.getFileName());
//        }

//        List<String> namesForSearch = new ArrayList<String>();
//        namesForSearch.add("CC");
//        namesForSearch.add("uzas");
//
//        boolean flag=local.listDirForNames("",namesForSearch);
//        Fajl fajl = local.findDirectoryOfFile("B.txt");
//        System.out.println(fajl);

//        List<Fajl> input = new ArrayList<>();
//        input.add(new Fajl("test",".txt","/root/Desktop/vid/joka/",LocalDate.now(),LocalDate.of(2022,2,10),2000));
//        input.add(new Fajl("test2",".jpg","/root/Desktop/vid/joka2/",LocalDate.now(),LocalDate.of(2022,2,10),2000));
//        input.add(new Fajl("test3",".png","/root/Desktop/vid/joka3/",null,null,2000));
//        input.add(new Fajl("test4",".svg","/root/Desktop/vid/joka4/",null,null,2000));
//        input.add(new Fajl("test5",".docx","/root/Desktop/vid/joka5/",null,null,2000));
//
//        List<Fajl> filtered = local.filterData(input, true, true, false, false ,false, false);
//
//        for(Fajl fajl : filtered){
//            System.out.println(fajl);
//        }

//        local.moveFiles("C:\\Users\\Vid\\Desktop\\test","C:\\Users\\Vid\\Desktop\\tt\\");
//        local.rename("novi","noviFolder");
//        List<Fajl> input = new ArrayList<>();
//        input.add(new Fajl("b",".txt","/root/Desktop/vid/joka/",LocalDate.of(2022,2,18),LocalDate.of(2022,2,10),2000));
//        input.add(new Fajl("c",".jpg","/root/Desktop/vid/joka2/",LocalDate.of(2022,2,10),LocalDate.of(2022,2,10),2000));
//        input.add(new Fajl("a",".png","/root/Desktop/vid/joka3/",LocalDate.of(2022,1,5),null,2000));
//        input.add(new Fajl("d",".svg","/root/Desktop/vid/joka4/",LocalDate.of(2022,1,12),null,2000));
//        input.add(new Fajl("e",".docx","/root/Desktop/vid/joka5/",LocalDate.of(2022,3,10),null,2000));

//        List<Fajl> out = local.sort(input,true,false,false,true);
//
//        for(Fajl fajl : out)
//            System.out.println(fajl + fajl.getCreationDate().toString());
//
//        List<Fajl> out = local.listFileByDate("2022-11-05", "C:\\Users\\Vid\\Desktop\\joka");
//        out = local.sort(out, true, false, false, true);
//
//        for(Fajl fajl : out)
//            System.out.println(fajl.getFileName());

//        List<Fajl> results = local.listFilesBetweenDates("2022-10-31","2022-10-31","C:\\Users\\Vid\\Desktop\\joka");
//
//        for(Fajl fajl : results)
//            System.out.println(fajl);

//        local.buildPath("C:\\Users\\Vid\\Desktop\\test2\\test3\\test.txt");
//        local.createFile("C:\\Users\\Vid\\Desktop\\test\\test2\\test3\\","test.txt");
//
//        Fajl out = local.findDirectoryOfFile("test1.txt");
//        System.out.println(out.toString());
    }

}
