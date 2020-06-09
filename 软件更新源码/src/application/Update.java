package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Update {
	/**
	 * 
	 * @param fileUrl ������URL
	 * @param filePath�����ļ�·��
	 * @return
	 * @throws MalformedURLException
	 */
	public static boolean partUpdate(URL fileUrl,String filePath) throws MalformedURLException {
		List<String> oldFileList = new ArrayList<String>();   //�����ļ�
		List<String> serverFileList = new ArrayList<String>();  //�������ļ�
		File localFile = new File(filePath+"/cfile.txt");	
		oldFileList = searchAllFile(localFile);
		URL temfileUrl = new URL(fileUrl + "/tem.txt");   
		downloadFile(temfileUrl,filePath + "/temFile.txt");
		File serverFile = new File(filePath  + "/temFile.txt");
		serverFileList = searchAllFile(serverFile);
		/*�ȶ�*/
		for(String sFile:serverFileList) {
			if(!oldFileList.contains(sFile)){
				URL subfileUrl = new URL(fileUrl + "/" + sFile);  //ȱʧ���ļ�URL
				if(sFile.lastIndexOf(".") == -1) {
					 downloadUpdate(filePath + "/" + sFile);
				}else
				    downloadFile(subfileUrl,filePath + "/" + sFile );
			}
		}
		for(String oldfile:oldFileList) {
			if(!serverFileList.contains(oldfile)) {
				File delFile = new File(filePath + "/" + oldfile);
				delFile.delete();
			}
		}
		return true;
	}
	
	/**
	 * read the file
	 * @param file
	 * @return 
	 */
	public static List<String> searchAllFile(File file){
		List<String> fileInfoList = new ArrayList<String>();
		try{			
			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);
			//����־�ļ��е��ļ���־����
			String s;
			while((s = bReader.readLine()) != null){				
				fileInfoList.add(s);
			}
			bReader.close();
			fReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return fileInfoList;
	}
	 /**
	  * download the file by given URL
	  * @param fileUrl
	  * @param fileName  Ҫд����ļ�����ͨ��new�½�
	  */
	//��һ������Ϊ����·�����ڶ�������Ϊ�ļ���
		public static void downloadFile(URL fileUrl,String fileName){
			InputStream is = null;
			OutputStream os = null;
			try {						
				URLConnection uric = fileUrl.openConnection();
				int fileLength = uric.getContentLength();
				if(fileLength >= 0){
					byte[] buffer = new byte[fileLength];
					is = uric.getInputStream();			
					
					is.read(buffer, 0, fileLength);
					//���ز�����
					File file = new File("./localsoftfile/soft v1.0/" + fileName);				
					if(file.exists()){
						file.delete();					
					}
					file.createNewFile();
					os = new FileOutputStream(file);

					os.write(buffer);
					os.close();										
				}else{
					JOptionPane.showMessageDialog(null,"Ҫ���ص��ļ�Ϊ���ļ���","",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}catch(Exception e){
				System.out.println(e);
			}
			finally{
				try {
					if(is != null)
					    is.close();
					if(os != null)
						os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				
			}
		}
		public static boolean downloadUpdate(String directory) throws MalformedURLException{
			
			//�����ļ���
			String pathString = "file:///C:/Users/bigzo/eclipse-workspace/2019-8-9/updatefile1/soft v2.0/";
			File temFilePath = new File("./localsoftfile/soft v1.0/" + directory);
			
			if(temFilePath.exists()){
				temFilePath.delete();
			}
			
			temFilePath.mkdir();
										
		    File serverFile = new File("./updatefile1/soft v2.0/" + directory);
		    File [] serverFiles = serverFile.listFiles();
			for(int i = 0;i < serverFiles.length;i++){
				System.out.println("1");
				URL fileUrl1 = new URL(pathString + directory+"/" + serverFiles[i].getName().toString() );
				downloadFile(fileUrl1,directory + "/" + serverFiles[i].getName().toString());
			}
			return true;
		}

	
	/**
	 * �ȶ԰汾��
	 * @param localConfPath���������ļ�·��
	 * @param serverConfUrl�����������ļ�url
	 * @return
	 * @throws Exception
	 */
	public static boolean compareVersion(String localConfPath, String serverConfUrl) throws Exception {
		File localConfi = new File(localConfPath);
		String temFileString = "temFile.properties";
		URL serverUrl = new URL(serverConfUrl);
		Update.downloadFile(serverUrl,temFileString );
		File temServerFIle = new File("./localsoftfile/soft v1.0/" + temFileString);
		Properties properties1 = new Properties();
		Properties properties2 = new Properties();
		properties1.load(new FileInputStream(localConfi));						
		properties2.load(new FileInputStream(temServerFIle));						
		double version1 = Double.parseDouble(properties1.getProperty("version"));
		double version2 = Double.parseDouble(properties2.getProperty("version"));
		if(!localConfi.exists()|!temServerFIle.exists()){
			JOptionPane.showMessageDialog(null,
				    "�汾�Ƚϳ����쳣��","",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		//��ʾ����ʾ���������ļ��ɸ���
		if(version1 < version2) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * �����ļ����²��Ҹ��±��������ļ�
	 * @param localConfPath
	 * @param tempServerConfpath
	 * @throws Exception
	 */
	public static void partupdate(String localConfPath, String tempServerConfpath) throws Exception{
		File localConfi = new File(localConfPath);
		File temServerConf = new File("./localsoftfile/soft v1.0/" + tempServerConfpath);
		Properties properties1 = new Properties();
		Properties properties2 = new Properties();
		properties1.load(new FileInputStream(localConfi));						
		properties2.load(new FileInputStream(temServerConf));
		/*�����ļ�*/
		if(!properties1.getProperty("updatedirectory1").equals(properties2.getProperty("updatedirectory1")) 
				){
			downloadUpdate("updatefile1");
		}
		if(!properties1.getProperty("updatedirectory2").equals(properties2.getProperty("updatedirectory2")) 
				){
			downloadUpdate("updatefile2");
		}
		if(!properties1.getProperty("updatedirectory3").equals(properties2.getProperty("updatedirectory3")) 
				){
			downloadUpdate("updatefile4");
		}
		
		if(!properties1.getProperty("filename").equals(properties2.getProperty("filename"))){
			URL fileUrl = new URL("file:///C:/Users/bigzo/eclipse-workspace/2019-8-9/updatefile1/soft v2.0/" + properties2.getProperty("filename"));
			downloadFile(fileUrl, properties2.getProperty("filename"));
			File oldFile = new File(properties1.getProperty("filename"));
			if(oldFile.exists()){
				oldFile.delete();
			}
		}
		/*�޸ı���conf�ļ�*/
		byte[] buffer = new byte[1024];
		int temp;
		FileInputStream iStream = new FileInputStream(temServerConf);
		FileOutputStream os = new FileOutputStream(localConfi);
		while((temp = iStream.read(buffer))!= -1) {
			os.write(buffer, 0, temp);
		}
		iStream.close();
		os.close();
	}
}

