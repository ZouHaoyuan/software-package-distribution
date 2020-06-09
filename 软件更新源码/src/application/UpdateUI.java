package application;

import java.awt.CheckboxGroup;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateUI extends GridPane{
	CheckBox confiCheckBox1;
	CheckBox confiCheckBox2;
	TextArea textArea1;
	TextArea textArea2;
	BorderPane borderPane2;
	final ChoiceBox<String> choiceUpdateBox;
	public UpdateUI(){
		super();
		this.setHeight(600);
		this.setWidth(1000);
		this.setLayoutX(1);
		this.setLayoutY(2);
		this.setHgap(30);
		this.setVgap(10);

		BorderPane borderPane1 = new BorderPane();
		HBox hBox1 = new HBox();
		hBox1.setPadding(new Insets(15, 15, 15, 15));
		hBox1.setSpacing(10);
		Label updatelabel = new Label("更新选择");
		updatelabel.setStyle("-fx-font: 18 arial;");
		Button getNewConfiButton = new Button("获取最新文件信息");
		getNewConfiButton.setPadding(new Insets(8));
		/*检查更新信息*/
		getNewConfiButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub			
				String filePathString = "updatefile1";
				File serverFile = new File(filePathString);
				if(serverFile.exists()&&serverFile.isDirectory()){
					File[] serverFiles = serverFile.listFiles();
					if (serverFiles.length != 0){
						for(int i = 0 ; i< serverFiles.length;i++){
							String fileInfo = "文件名：" + serverFiles[i].getName() + "\n文件大小：" + serverFiles[i].length() 
									+ "\n修改日期：" + serverFiles[i].lastModified()+ "\n\n";						
							textArea1.appendText(fileInfo);							
						}
						//提示获取最新配置成功
			        	JOptionPane.showMessageDialog(null,
							    "获取最新文件成功！","",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				else
					JOptionPane.showMessageDialog(null,
							"暂时无可更新文件！","",
							JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
//		getNewConfiButton.set
		hBox1.getChildren().add(updatelabel);
		hBox1.getChildren().add(getNewConfiButton);
		borderPane1.setTop(hBox1);
		
		ScrollPane scrollPane1 = new ScrollPane();
		textArea1 = new TextArea();
		scrollPane1.setContent(textArea1);
		TitledPane titledPane1 = new TitledPane("最新文件信息",scrollPane1);
		titledPane1.setPadding(new Insets(10));
		borderPane1.setCenter(titledPane1);
		
		ScrollPane scrollPane2 = new ScrollPane();
		textArea2 = new TextArea();
		scrollPane2.setContent(textArea2);
		TitledPane titledPane2 = new TitledPane("本地文件信息",scrollPane2);
		titledPane2.setPadding(new Insets(10));
		borderPane1.setBottom(titledPane2);
		this.add(borderPane1, 0, 0);
		
		borderPane2 = new BorderPane();		
		
		HBox hBox2 = new HBox();
		hBox2.setPadding(new Insets(10));
		Label choiceWaylabel = new Label("选择更新方式：");
		choiceUpdateBox = new ChoiceBox<String>(
			        FXCollections.observableArrayList("整体更新", "部分更新"));

		choiceUpdateBox.getSelectionModel().selectedIndexProperty()
		.addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number value, Number new_value) {
//			            if(new_value.intValue()==1){
//			            	Label conLabel = new Label("对更新文件选择");
//			            	confiCheckBox1 = new CheckBox("configuration1");
//			            	VBox.setMargin(confiCheckBox1, new Insets(10, 0, 0, 0));
//			        		confiCheckBox2 = new CheckBox("configuration2");
//			        	    VBox.setMargin(confiCheckBox2, new Insets(10, 0, 0, 0));
//			        		VBox checkVBox = new VBox();
//			        		checkVBox.setPadding(new Insets(250, 10, 10, 50));
//			        		checkVBox.getChildren().addAll(conLabel,confiCheckBox1,confiCheckBox2);
//			        		borderPane2.setCenter(checkVBox);
//			            }else if(new_value.intValue() == 0){
//			            	borderPane2.setCenter(null);
//			            }
			          }
			        });

		choiceUpdateBox.setTooltip(new Tooltip("选择更新方式"));
		choiceUpdateBox.setValue("中文");
		Button updateButton = new Button("确定更新");
		HBox.setMargin(updateButton, new Insets(0, 0, 0, 20));
		updateButton.setPadding(new javafx.geometry.Insets(8));
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(choiceUpdateBox.getSelectionModel().getSelectedIndex()==0){
					//进行整体更新
					try {
						Runtime.getRuntime().exec("java -jar " + "open.jar");
						System.exit(0);
					} catch (IOException e){
						System.out.println(e);
					}
				}else{
					//进行部分更新
					
					try {
						File localConfi = new File("configuration1.properties");
						String temFileString = "temFile.properties";
						/*URL serverUrl = new URL("file:///C:/Users/bigzo/eclipse-workspace/2019-8-9/updatefile1/configuration.properties");
						downloadFile(serverUrl,temFileString );
						File temServerFIle = new File("./localsoftfile/soft v1.0/" + temFileString);
						Properties properties1 = new Properties();
						Properties properties2 = new Properties();
						properties1.load(new FileInputStream(localConfi));						
						properties2.load(new FileInputStream(temServerFIle));						
						double version1 = Double.parseDouble(properties1.getProperty("version"));
						double version2 = Double.parseDouble(properties2.getProperty("version"));
						/*版本号比对*/
						/*if(version1 >= version2) {
							JOptionPane.showMessageDialog(null,
								    "已经是最新版本！","",
									JOptionPane.INFORMATION_MESSAGE);
							return ;
						}
						*/
						String localConfPath = "./localsoftfile/configuration1.properties";
						String serverConfUrl = "file:///C:/Users/bigzo/eclipse-workspace/2019-8-9/updatefile1/configuration.properties";
						if(!Update.compareVersion(localConfPath, serverConfUrl)) {
							JOptionPane.showMessageDialog(null,
								    "已经是最新版本！","",
									JOptionPane.INFORMATION_MESSAGE);
							return ;
						}
						/*更新*/
						Update.partupdate(localConfPath, "temFile.properties");
						/*
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
						*/														
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e);
					} 
					JOptionPane.showMessageDialog(null,
						    "文件更新成功！","",
							JOptionPane.INFORMATION_MESSAGE);					 					
				}
			}
		});
		hBox2.getChildren().addAll(choiceWaylabel,choiceUpdateBox,updateButton);
		borderPane2.setBottom(hBox2);
		this.add(borderPane2, 1, 0);
		
		//执行查找本地配置文件并显示
		try {
			queryLocalConfi();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void queryLocalConfi() throws Exception{
		File confiFile = new File("./localsoftfile/configuration1.properties");		
			if(confiFile.exists()){
				Properties properties1 = new Properties();
				properties1.load(new FileInputStream(confiFile));
				textArea2.appendText(confiFile.getName()+"\n"+properties1.getProperty("name")+"\n"+properties1.getProperty("version"));								
			}

		}
	

}
