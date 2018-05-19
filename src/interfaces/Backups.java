/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author ANURUDDHA
 */
public class Backups {

    public static void restoreBackup(String mysqlPath,String dbUsername,String dbPassword,String source) {
//        String mysqlPath = "C:\\wamp\\bin\\mysql\\mysql5.6.17\\bin\\mysql";
//        String dbUsername = "root";
//        String dbPassword = "";
        source = source+"/backup.sql";
        mysqlPath=mysqlPath+"/mysql";
        System.out.println(mysqlPath);
        System.out.println(dbUsername);
        System.out.println(dbPassword);
        System.out.println(source);
        
        String[] restoreCmd = new String[]{"" + mysqlPath + "", "--user=" + dbUsername, "--password=" + dbPassword, "-e", "source " + source};
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(restoreCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("Restored successfully!");
            } else {
                System.out.println("Could not restore the backup!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void createBackup() {
        String mysqldumpPath = "C:\\wamp\\bin\\mysql\\mysql5.6.17\\bin\\mysqldump";
        String dbUsername = "root";
        String dbName = "expiretest";
        String filePath = "C:\\c\\";
        String fileName = "backup";

        Process p = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec("" + mysqldumpPath + " -u" + dbUsername + " " + " --add-drop-database -B " + dbName + " -r " + filePath + "" + fileName + ".sql");
            int processComplete = p.waitFor();
            if (processComplete == 0) {
                System.out.println("Backup created successfully!");
            } else {
                System.out.println("Could not create the backup");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
