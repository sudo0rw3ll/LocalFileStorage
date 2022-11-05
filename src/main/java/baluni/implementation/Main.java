package baluni.implementation;

import baluni.filestorage.StorageConfig;
import baluni.model.Fajl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        LocalStorage local = new LocalStorage();
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

        List<String> forbidden = new ArrayList<>();
        forbidden.add(".jpg");
        forbidden.add(".png");
        forbidden.add(".svg");
        StorageConfig storageConfig = new StorageConfig("Moj storage", 4096,10, forbidden);
        local.setStorageConfig(storageConfig);
        local.setSotragePath("C:\\Users\\Vid\\Desktop\\test\\");
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

        List<Fajl> input = new ArrayList<>();
        input.add(new Fajl("b",".txt","/root/Desktop/vid/joka/",LocalDate.of(2022,2,18),LocalDate.of(2022,2,10),2000));
        input.add(new Fajl("c",".jpg","/root/Desktop/vid/joka2/",LocalDate.of(2022,2,10),LocalDate.of(2022,2,10),2000));
        input.add(new Fajl("a",".png","/root/Desktop/vid/joka3/",LocalDate.of(2022,1,5),null,2000));
        input.add(new Fajl("d",".svg","/root/Desktop/vid/joka4/",LocalDate.of(2022,1,12),null,2000));
        input.add(new Fajl("e",".docx","/root/Desktop/vid/joka5/",LocalDate.of(2022,3,10),null,2000));

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

        local.buildPath("C:\\Users\\Vid\\Desktop\\test2\\test3\\test.txt");
        local.createFile("C:\\Users\\Vid\\Desktop\\test\\test2\\test3\\","test.txt");
    }

}
