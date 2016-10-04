package org.jsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import io.sealights.FileAndFolderUtils;
import io.sealights.PathUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.SystemTestCase4;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;

public class FileOperations extends SystemTestCase4 {

    /*@Test
    public void verifyFileExistence(){
        File file = new File("pom.xml");
        Assert.assertTrue(file.exists());
    }*/

    private String fileName = "pom.xml";

    @Test
    public void verifyFileExistence() {
        File file = new File(fileName);
        Assert.assertTrue(file.exists());
    }

    @Test
    public void verifyFalse() {

        Assert.assertTrue(false);
    }

    @Test
    public void server_defaultEndpoint_shouldReturnHelloWorld() throws Exception {
        String userDir = System.getProperty("user.dir");
        String targetFolder = userDir + File.separator + "target";
        String agentPath = getAgentJarPath();
        StartedProcess javaProcess = null;
        try {
            javaProcess = runAgent(targetFolder, agentPath, "my-tests-proj-1.0-SNAPSHOT.jar", "io.demo.App");
            sleep(3 * 1000);

            HttpClient client = new HttpClient();
            client.start();

            Request res = client.newRequest("http://localhost:8081");
            res.header(HttpHeader.HOST, "text/plain");
            res.method(HttpMethod.GET);

            ContentResponse response = res.send();

            String actual = response.getContentAsString();
            Assert.assertEquals("Hello World", actual);

            sleep(10 * 1000);

        } finally {
            if (javaProcess != null) {
                javaProcess.getProcess().destroy();
            }

        }


    }

    public StartedProcess runAgent(String workingDirectory, String agentPath, String targetJar, String mainClass) {

        ProcessExecutor processExecutor = createProcessExecuter(workingDirectory);

        FileOutputStream outputStream = createLogsOutputStream(workingDirectory);
        FileOutputStream errorStream = createLogsErrorStream(workingDirectory);

        String javaAgentPath = "-javaagent:" + agentPath;

        List<String> command = getCommand(javaAgentPath, targetJar, mainClass);
        try {
            return processExecutor.command(command).redirectOutput(outputStream).redirectError(errorStream).start();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private ProcessExecutor createProcessExecuter(String workingDirectory) {
        ProcessExecutor processExecutor = new ProcessExecutor();
        processExecutor.readOutput(true);
        processExecutor.directory(new File(workingDirectory));
        return processExecutor;

    }

    private FileOutputStream createLogsOutputStream(String workingDirectory) {
        try {
            String logPath = PathUtils.join(workingDirectory, "log.txt");
            File logFile;
            logFile = FileAndFolderUtils.getOrCreateFile(logPath);
            return new FileOutputStream(logFile, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private FileOutputStream createLogsErrorStream(String workingDirectory) {
        try {
            String errorLogPath = PathUtils.join(workingDirectory, "error-log.txt");
            File errorLogFile = FileAndFolderUtils.getOrCreateFile(errorLogPath);
            return new FileOutputStream(errorLogFile, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getCommand(String javaAgentPath, String targetJar, String mainClass) {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-Dsl.server=" + System.getProperty("server"));
        command.add("-Dsl.appName=" + System.getProperty("appName"));
        command.add("-Dsl.customerId=" + System.getProperty("customerId"));
        command.add("-Dsl.includes=" + System.getProperty("includes"));
        command.add("-Dsl.log.enabled=true");
        command.add("-Dsl.log.level=info");
        command.add("-Dsl.log.toConsole=true");
        command.add("-Dsl.interval=1000");
        command.add(javaAgentPath);
        command.add("-cp");
        command.add(targetJar);
        command.add(mainClass);
        return command;
    }


    private String getAgentJarPath() {
        String agentPath = System.getProperty("agentPath");
        if (agentPath != null && agentPath.length() > 0)
            return agentPath;
        return "C:\\Work\\Projects\\SL.OnPremise.Agents.Java\\java-agent-bootstrapper\\target\\java-agent-bootstrapper-1.0.0-SNAPSHOT-jar-with-dependencies.jar";
    }

    private void sleep(int timeoutInMs) {
        try {
            Thread.sleep(timeoutInMs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
