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
		    Menu updateMenu = new Menu("更新");
		    MenuItem updateMenuItem = new MenuItem("进入更新界面");
		    /*添加跟新界面*/
		    updateMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					updateUI = new UpdateUI();
					rootPane.setCenter(updateUI);
					stage.setTitle("更新界面v1.1");
				}
			});
		    updateMenu.getItems().add(updateMenuItem);
		    rootPane.setTop(menuBar);
		    
			stage = primaryStage;
			GridPane rootGridPane = new UpdateUI();
			rootPane.setCenter(rootGridPane);
			stage.setScene(new Scene(rootPane,1000,600));
			stage.setTitle("更新界面v1.1");
			stage.show();
			
			Properties properties = new Properties();
			properties.load(new FileInputStream("configuration1.properties"));
			String keyString1 = properties.getProperty("softname");
			String keyString2 = properties.getProperty("version");
			
			//检查是否有文件可更新
			String localConfPath = "./localsoftfile/configuration1.properties";
			String serverConfUrl = "file:///C:/Users/bigzo/eclipse-workspace/2019-8-9/updatefile1/configuration.properties";
			if(Update.compareVersion(localConfPath, serverConfUrl)) {
				JOptionPane.showMessageDialog(null,"软件名称：" + keyString1 + "\n版本：" + keyString2
						+ "\n" + "有文件可更新！","",
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
