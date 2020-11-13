import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.Timer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;



public class MSGUI extends Application {
	// create model and gridpane attributes so they can be accessed in the listeners
	private Label curTime;
	private Label numMinesLeft;
	private MenuItem begGame;
	private MenuItem intGame;
	private MenuItem expertGame;
	private MenuItem quit;
	private MenuItem setNumMines;
	private MenuItem howToPlay;
	private MenuItem about;
	private VBox vbox;
	private VBox menFace;
	ImageView face = new ImageView();
	boolean firstClick = true;
	long now = 0; 
	long old = 0;
	int secs = 0;
	ToolBar bottomTools;
	private MSTimer timer= new MSTimer();

	
	private MinesweeperModel ms= new MinesweeperModel (8, 8, 10);
	private int numMines = ms.totalMines();
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("Minesweeper");
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 800, 600);
		bottomTools = new ToolBar();
		curTime = new Label();
		curTime.setText("Time Elapsed: " + 0);
		face = new ImageView(new Image(new FileInputStream("MSImage/face_smile.gif")));
		face.setTranslateX((800-53)/2);
		face.setTranslateY(600 - 32*15);
		numMinesLeft = new Label();
		numMinesLeft.setText("Mines left: " + numMines);
		bottomTools.getItems().addAll(curTime, new Label(" | "), numMinesLeft);
		quit = new MenuItem("Exit");
		quit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				System.exit(0);
			}
		});
		
		setNumMines = new MenuItem("Set Number of Mines");
		setNumMines.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				TextInputDialog alert = new TextInputDialog("xD");
				alert.setContentText("ENTER MINES NASDF");
				int numMines = Integer.parseInt(alert.showAndWait().get());
				if(numMines > ms.getCols()*ms.getRows() || numMines < 0){
					TextInputDialog error = new TextInputDialog("ok");
					error.setContentText("Invalid Amount");
					error.showAndWait();
				}else{
					ms = new MinesweeperModel(ms.getRows(), ms.getCols(), numMines);
				}
			}
		});
		howToPlay = new MenuItem("How to Play");
		about = new MenuItem("About");
		
		Menu game = new Menu("Game");
		Menu options = new Menu("Options");
		Menu help = new Menu("Help");
		MenuBar topMenu = new MenuBar();
		 menFace = new VBox();
		root.setBottom(bottomTools);
		root.setTop(menFace);
       vbox = new VBox();
       for(int i = 0; i < ms.getRows(); i++){
    	   vbox.getChildren().add(wew(ms.getCols()));
       }
       vbox.setTranslateX((800-(32 * ms.getCols())) / 2);
       vbox.setTranslateY(((600 - (32 * ms.getRows())) / 2) - 50);

       vbox.setOnMouseClicked(new EventHandler<MouseEvent>(){

		@Override
		public void handle(MouseEvent event) {
			int col = (int)event.getX()/32;
			int row = (int)event.getY()/32;
			if(!ms.isGameWon()){
				
				if(event.getButton() == MouseButton.PRIMARY){
					if(firstClick){
						timer.start();
						firstClick = false;
					}
					if(ms.isMine(row, col)){
						timer.stop();
						System.out.println("YOU LOSE!");
						try {
							face = new ImageView(new Image(new FileInputStream("MSImage/face_dead.gif")));
							face.setTranslateX((800-53)/2);
							face.setTranslateY(600 - 32*15);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						menFace.getChildren().set(1, face);
						ms.revealAll();
						update();
					}else if(!ms.isFlag(row, col)){
						ms.reveal(row, col);
						update();
					}
				}else if(event.getButton() == MouseButton.SECONDARY){
					if(ms.isFlag(row, col)){
						ms.setFlag(row, col, false);
						numMines++;
						numMinesLeft.setText("Mines left: " + numMines);
						bottomTools.getItems().set(2,  numMinesLeft);
					}else if(!ms.isFlag(row, col) && !ms.isRevealed(row, col)){
						ms.setFlag(row, col, true);
						numMines--;
						numMinesLeft.setText("Mines left: " + numMines);
						bottomTools.getItems().set(2,  numMinesLeft);
					}
					update();
				}
			}else{
				System.out.println("YOU WIN!!!");
				try {
					face = new ImageView(new Image(new FileInputStream("MSImage/face_win.gif")));
					face.setTranslateX((800-53)/2);
					face.setTranslateY(600 - 32*15);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				menFace.getChildren().set(1, face);
				update();
			}
		}
       });
       
		begGame = new MenuItem("New Beginner Game");
		begGame.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				ms = new MinesweeperModel(8,8,10);
				numMines = ms.totalMines();
				numMinesLeft.setText("Mines left: " + numMines);
				bottomTools.getItems().set(2,  numMinesLeft);
				try {
					face = new ImageView(new Image(new FileInputStream("MSImage/face_smile.gif")));
					face.setTranslateX((800-53)/2);
					face.setTranslateY(600 - 32*15);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				menFace.getChildren().set(1, face);
				firstClick = true;
				now = 0; 
				old = 0;
				secs=0;
				update();	
				curTime.setText("Time Elapsed: " + 0);
				bottomTools.getItems().set(0,curTime);
			}
		});
		
		intGame = new MenuItem("New Intermediate Game");
		expertGame = new MenuItem("New Expert Game");		
		menFace.getChildren().addAll(topMenu, face);
		
		
		game.getItems().addAll(begGame, intGame, expertGame, quit);
		options.getItems().add(setNumMines);
		help.getItems().addAll(howToPlay, about);
				
		Stage abStage = new Stage();
		VBox abRoot = new VBox();
		abRoot.setAlignment(Pos.BOTTOM_CENTER);
		abRoot.setStyle("-fx-background-color: grey");
		Scene abScene = new Scene(abRoot, 300,200);
		Button abClose = new Button("OK");
		abClose.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				abStage.close();
			}
			
		});
		WebView abView = new WebView();
		WebEngine webEngine = new WebEngine();
		File abFile = new File("aboutpage.html");
		webEngine.load("file:///" + abFile.getAbsolutePath());
		abRoot.getChildren().addAll(abView, abClose);
		abStage.setScene(abScene);
		
		topMenu.getMenus().addAll(game, options, help);
		root.setCenter(vbox);
		stage.setScene(scene);
		stage.show();
		
	}
	public void update(){
		for(int i = 0 ; i < ms.getRows(); i++){
			for(int j = 0; j<ms.getCols(); j++){
				try{
					if(ms.isFlag(i, j)){
						((HBox)(vbox.getChildren().get(i))).getChildren().set(j, new ImageView(new Image(new FileInputStream("MSImage/bomb_flagged.gif"))));
					}else if(ms.isMine(i, j) && ms.isRevealed(i,j)){
						((HBox)(vbox.getChildren().get(i))).getChildren().set(j, new ImageView(new Image(new FileInputStream("MSImage/bomb_death.gif"))));
					}else if(ms.isRevealed(i,j)){
						((HBox)(vbox.getChildren().get(i))).getChildren().set(j, new ImageView(new Image(new FileInputStream("MSImage/num_" + ms.getNumNeighboringMines(i, j) + ".gif"))));
					}
					else{
						((HBox)(vbox.getChildren().get(i))).getChildren().set(j, new ImageView(new Image(new FileInputStream("MSImage/blank.gif"))));
					}
					
					if(ms.isGameWon()){
						System.out.println("YOU WIN!!!");
						try {
							face = new ImageView(new Image(new FileInputStream("MSImage/face_win.gif")));
							face.setTranslateX((800-53)/2);
							face.setTranslateY(600 - 32*15);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						menFace.getChildren().set(1, face);
						break;
					}
				}catch(IOException e){
					
				}
			}
		}
	}
	private class MSTimer extends AnimationTimer{

		@Override
		public void handle(long now) {
			// TODO Auto-generated method stub
			if(now -old > 1200000000){
				secs ++;
				curTime.setText("Time Elapsed: " + secs);
				bottomTools.getItems().set(0, curTime);
				old = now;
			}
			
		}
		
	}
	
	public HBox makeHB(int ooh){
		HBox hBox = new HBox();
		for(int i = 0; i<ooh; i++){
	    	   Image image = null;
			try {
				image = new Image(new FileInputStream("MSImage/blank.gif"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	   ImageView iV = new ImageView();
	           iV.setImage(image);
	        hBox.getChildren().add(iV);
	       }
     	return hBox;
	}
}
