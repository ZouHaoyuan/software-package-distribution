package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	Stage stage;
	BorderPane rootPane; 
	UpdateUI updateUI;
	@Override
	public void start(Stage primaryStage) {
		try {
			rootPane = new BorderPane();
			MenuBar menuBar = new MenuBar();
		    menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		    rootPane.setTop(menuBar);
		    Menu updateMenu = new Menu("����");
		    MenuItem updateMenuItem = new MenuItem("������½���");
		    /*��Ӹ��½���*/
		    updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					updateUI = new UpdateUI();
					rootPane.setCenter(updateUI);
					stage.setTitle("���½���v1.1");
				}
			});
		    updateMenu.getItems().add(updateMenuItem);
		    rootPane.setTop(menuBar);
		    
			stage = primaryStage;
			GridPane rootGridPane = new UpdateUI();
			rootPane.setCenter(rootGridPane);
			stage.setScene(new Scene(rootPane,1000,600));
			stage.setTitle("���½���v1.1");
			stage.show();
			
			Properties properties = new Properties();
			properties.load(new FileInputStream("configuration1.properties"));
			String keyString1 = properties.getProperty("softname");
			String keyString2 = properties.getProperty("version");
			
			//����Ƿ����ļ��ɸ���
			String localConfPath = "./localsoftfile/configuration1.properties";
			String serverConfUrl = "file:///C:/Users/bigzo/eclipse-workspace/2019-8-9/updatefile1/configuration.properties";
			if(Update.compareVersion(localConfPath, serverConfUrl)) {
				JOptionPane.showMessageDialog(null,"������ƣ�" + keyString1 + "\n�汾��" + keyString2
						+ "\n" + "���ļ��ɸ��£�","",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}	
	
}
