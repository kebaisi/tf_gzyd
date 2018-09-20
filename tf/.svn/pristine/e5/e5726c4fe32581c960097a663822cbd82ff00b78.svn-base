package com.kbs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class DatabaseBackup {


private String username;


private String password;

public String getUsername() {
   return username;
}

public void setUsername(String username) {
   this.username = username;
}

public String getPassword() {
   return password;
}

public void setPassword(String password) {
   this.password = password;
}

public DatabaseBackup() {
   super();
}

public DatabaseBackup(String username, String password) {
   super();
   this.username = username;
   this.password = password;
}


//public void backup(OutputStream output, String dbname) {
////   String command = "cmd /c " + mysqlBinPath + "mysqldump -u" + username + " -p" + password + " --set-charset=utf8 "
////     + dbname;
//	String command="start backup.bat ";
//   System.out.println(command);
//   PrintWriter p = null;
//   BufferedReader reader = null;
//   try {
//    p = new PrintWriter(new OutputStreamWriter(output, "utf8"));
//    Process process = Runtime.getRuntime().exec(command);
//    InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
//    reader = new BufferedReader(inputStreamReader);
//    String line = null;
//    while ((line = reader.readLine()) != null) {
//     p.println(line);
//    }
//    p.flush();
//   } catch (UnsupportedEncodingException e) {
//    e.printStackTrace();
//   } catch (IOException e) {
//    e.printStackTrace();
//   } finally {
//    try {
//     if (reader != null) {
//      reader.close();
//     }
//     if (p != null) {
//      p.close();
//     }
//    } catch (IOException e) {
//     e.printStackTrace();
//    }
//   }
//}
//
//
//public void backup(String dest, String dbname) {
//   try {
//    OutputStream out = new FileOutputStream(dest);
//    backup(out, dbname);
//   } catch (FileNotFoundException e) {
//    e.printStackTrace();
//   }
//}
//
//
//public void restore(InputStream input, String dbname) {
//   String command = "cmd /c " + mysqlBinPath + "mysql -u" + username + " -p" + password + " " + dbname;
//   try {
//    Process process = Runtime.getRuntime().exec(command);
//    OutputStream out = process.getOutputStream();
//    String line = null;
//    String outStr = null;
//    StringBuffer sb = new StringBuffer("");
//    BufferedReader br = new BufferedReader(new InputStreamReader(input, "utf8"));
//    while ((line = br.readLine()) != null) {
//     sb.append(line + "\r\n");
//    }
//    outStr = sb.toString();
//
//    OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
//    writer.write(outStr);
//    writer.flush();
//    out.close();
//    br.close();
//    writer.close();
//   } catch (UnsupportedEncodingException e) {
//    e.printStackTrace();
//   } catch (IOException e) {
//    e.printStackTrace();
//   }
//
//}
//
//
//public void restore(String dest, String dbname) {
//   try {
//    InputStream input = new FileInputStream(dest);
//    restore(input, dbname);
//   } catch (FileNotFoundException e) {
//    e.printStackTrace();
//   }
//}


public void backUp(String dPath){
	try {
		String path=GetProjectRealPath.getPath("backup.bat");
		File file = new File(path);
		System.out.println(file.getPath());
		String batName="cmd.exe /c start "+file.getPath()+" "+username+" "+password+" "+dPath;
//		batName=batName.replace("/", "//");
		System.out.println(batName);
		Process ps = Runtime.getRuntime().exec(batName);
//        InputStream in = ps.getInputStream();
//        int c;
//        while ((c = in.read()) != -1) {
//            System.out.print(c);// 如果你不需要看输出，这行可以注销掉
//        }
//        in.close();
        ps.waitFor();

    } catch (IOException ioe) {
        ioe.printStackTrace();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    System.out.println("child thread done");

}

public void restore(String dPath){
	try {
		String path=GetProjectRealPath.getPath("restore.bat");
		File file = new File(path);
		System.out.println(file.getPath());
		String batName="cmd.exe /c start "+file.getPath()+" "+username+" "+password+" "+dPath;
//		batName=batName.replace("/", "//");
		System.out.println(batName);
		Process ps = Runtime.getRuntime().exec(batName);
//        InputStream in = ps.getInputStream();
//        int c;
//        while ((c = in.read()) != -1) {
//            System.out.print(c);// 如果你不需要看输出，这行可以注销掉
//        }
//        in.close();
        ps.waitFor();

    } catch (IOException ioe) {
        ioe.printStackTrace();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    System.out.println("child thread done");

}

public int runBat(String fileName,String dPath){
	Process ps=null;
	try {
		String path=GetProjectRealPath.getPath(fileName);
		File file = new File(path);
		String batName="cmd.exe /c start "+file.getPath()+" "+username+" "+password+" "+dPath;
//		batName=batName.replace("/", "//");
		System.out.println(batName);
		ps = Runtime.getRuntime().exec(batName);
        InputStream in = ps.getInputStream();
        ps.waitFor();
    } catch (IOException ioe) {
        ioe.printStackTrace();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
	return ps.exitValue();
}

public static void main(String[] args) {
   DatabaseBackup bak = new DatabaseBackup("kbs", "kbs0755");
   bak.runBat("restore.bat","d:/tf-cal/tfbackup20150427.sql");
}
}