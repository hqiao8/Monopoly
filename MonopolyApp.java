import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.stage.Stage;

public class MonopolyApp extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		
		/*******************************
		 * Initialization of board positions, lists of card IDs, and player list
		 */
		ArrayList<Landmark> route = new ArrayList<Landmark>();
		for(int index = 0; index < 40; index++) {
			route.add(getLandmark(index));
		}
		ArrayList<Integer> chanceCardIDs = cardIDInitialization();
		ArrayList<Integer> communityChestCardIDs = cardIDInitialization();
		ArrayList<Player> players = new ArrayList<Player>();
		
		
		/*******************************
		 * WelcomeScene Generation: 
		 * Label: "WELCOME TO MONOPOLY!"
		 * Button: "Start The Game"
		 * Button: "Add A Player"
		 */
		Label welcomeLabel = new Label("WELCOME TO MONOPOLY!");
		Button startTheGame = new Button("Start The Game");
		Button addAPlayer = new Button("Add A Player");
		welcomeLabel.setFont(new Font("Arial", 50));
		startTheGame.setFont(new Font("Arial", 30));
		addAPlayer.setFont(new Font("Arial", 30));
		startTheGame.setDisable(true);
		VBox welcomeVBox = new VBox(20);
		welcomeVBox.getChildren().addAll(welcomeLabel, startTheGame, addAPlayer);
		welcomeVBox.setAlignment(Pos.CENTER);
		Scene welcomeScene = new Scene(welcomeVBox);
		
		
		/*******************************
		 * AddAPlayerScene Generation: 
		 * Token Panel: 4 *2 grid
		 * Label: "Please select a token and enter your username:"
		 * Button: Confirm adding a player
		 * Button: Cancel adding a player
		 */
		GridPane addAPlayerPanel = new GridPane();
		RowConstraints tokenRowConstraints = new RowConstraints();
		RowConstraints usernameRowConstraints = new RowConstraints();
		ColumnConstraints tokenColumnConstraints = new ColumnConstraints();
		tokenRowConstraints.setPercentHeight(35);
		usernameRowConstraints.setPercentHeight(15);
		tokenColumnConstraints.setPercentWidth(25);
		addAPlayerPanel.getRowConstraints().add(tokenRowConstraints);
		addAPlayerPanel.getRowConstraints().add(tokenRowConstraints);
		addAPlayerPanel.getRowConstraints().add(usernameRowConstraints);
		addAPlayerPanel.getRowConstraints().add(usernameRowConstraints);
		for(int j = 0; j < 4; j++) {
			addAPlayerPanel.getColumnConstraints().add(tokenColumnConstraints);
		}
		ToggleGroup group = new ToggleGroup();
		String[] tokens = {"Batman", "Captain America", "Flash", "Green Lantern", "Hulk", "Iron Man", "Robocop", "Spider-man"};
		for(int j = 0; j < 8; j++) {
			Image tempImage = new Image("Tokens/" + tokens[j] + ".jpg");
			RadioButton tempButton = new RadioButton(tokens[j]);
			ImageView tempImageView = new ImageView(tempImage);
			tempImageView.setFitWidth(150);
			tempImageView.setFitHeight(150);
			tempButton.setGraphic(tempImageView);
			tempButton.setContentDisplay(ContentDisplay.TOP);
			tempButton.setToggleGroup(group);
			addAPlayerPanel.add(tempButton, j%4, j/4);
		}
		Label usernameLabel = new Label("Please select a token and enter your username:");
		usernameLabel.setFont(new Font("Arial", 20));
		TextField usernameField = new TextField();
		HBox usernameBox = new HBox(20);
		usernameBox.getChildren().addAll(usernameLabel, usernameField);
		usernameBox.setAlignment(Pos.CENTER);
		addAPlayerPanel.add(usernameBox, 0, 2, 4, 1);
		Button confirmAddingAPlayer = new Button("Confirm");
		Button cancelAddingAPlayer = new Button("Cancel");
		confirmAddingAPlayer.setFont(new Font("Arial", 20));
		cancelAddingAPlayer.setFont(new Font("Arial", 20));
		confirmAddingAPlayer.setDisable(true);
		HBox confirmAddingAPlayerBox = new HBox(40);
		confirmAddingAPlayerBox.getChildren().addAll(confirmAddingAPlayer, cancelAddingAPlayer);
		confirmAddingAPlayerBox.setAlignment(Pos.CENTER);
		addAPlayerPanel.add(confirmAddingAPlayerBox, 0, 3, 4, 1);
		Scene addAPlayerScene = new Scene(addAPlayerPanel);
		
		
		/*******************************
		 * GameScene Generation: 
		 * Board Matrix: 
		 * Border Cells: Landmarks with description and color
		 * Center: Logo image
		 * Bottom Left: Two dice with a "Roll the dice" button
		 * Top Right: Player status pane with each player's money
		 * Bottom Right: Chance card deck with a "Draw A Card" button
		 * Top Left: Community chest card deck with a "Draw A Card" button
		 */
		GridPane board = new GridPane();
		RowConstraints borderRows = new RowConstraints();
		RowConstraints inbetweenRows = new RowConstraints();
		ColumnConstraints borderColumns = new ColumnConstraints();
		ColumnConstraints inbetweenColumns = new ColumnConstraints();		
		borderRows.setPercentHeight(14.0);
		inbetweenRows.setPercentHeight(8.0);
		borderColumns.setPercentWidth(14.0);
		inbetweenColumns.setPercentWidth(8.0);
		for (int i = 0; i < 11; i++) {
			if (i == 0 || i == 10) {
				board.getRowConstraints().add(borderRows);
			}
			else {
				board.getRowConstraints().add(inbetweenRows);
			}
		}
		for (int j = 0; j < 11; j++) {
			if (j == 0 || j == 10) {
				board.getColumnConstraints().add(borderColumns);
			}
			else {
				board.getColumnConstraints().add(inbetweenColumns);
			}
		}
		ImageView logo = new ImageView(new Image("Logo/Logo.jpg"));
		logo.setPreserveRatio(true);
		logo.setFitWidth(400);
		board.add(logo, 1, 4, 9, 3);
		GridPane.setHalignment(logo, HPos.CENTER);
		GridPane.setValignment(logo, VPos.CENTER);
        for(int index = 0; index < 40; index++) {
        	StackPane tempPane = new StackPane();
        	tempPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        	tempPane.setBackground(new Background(new BackgroundFill(route.get(index).getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
			Label tempLabel = new Label(route.get(index).getDescription());
			tempLabel.setFont(new Font("Arial", 10));
			tempLabel.setWrapText(true);
			tempPane.getChildren().add(tempLabel);
			board.add(tempPane, indexToSubscript(index)[0], indexToSubscript(index)[1]);
		}
        Dice dice1 = new Dice();
        Dice dice2 = new Dice();
        Button rollTheDice = new Button("Roll the dice");
        rollTheDice.setDisable(true);
        board.add(dice1.getFaceIcon(), 1, 7, 2, 2);
        GridPane.setHalignment(dice1.getFaceIcon(), HPos.CENTER);
		GridPane.setValignment(dice1.getFaceIcon(), VPos.CENTER);
        board.add(dice2.getFaceIcon(), 3, 7, 2, 2);
        GridPane.setHalignment(dice2.getFaceIcon(), HPos.CENTER);
		GridPane.setValignment(dice2.getFaceIcon(), VPos.CENTER);
		board.add(rollTheDice, 1, 9, 4, 1);
		GridPane.setHalignment(rollTheDice, HPos.CENTER);
		GridPane.setValignment(rollTheDice, VPos.CENTER);
		StackPane playerStatusPane = new StackPane();
		playerStatusPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		playerStatusPane.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		Label playerStatusLabel = new Label();
		playerStatusLabel.setFont(new Font("Arial", 15));
		playerStatusPane.getChildren().add(playerStatusLabel);
		StackPane.setAlignment(playerStatusLabel, Pos.TOP_LEFT);
		board.add(playerStatusPane, 6, 1, 4, 3);
		ImageView chanceCardsDeck = new ImageView(new Image("Chance Cards/Logo.jpg"));
		chanceCardsDeck.setPreserveRatio(false);
		chanceCardsDeck.setFitWidth(200);
		chanceCardsDeck.setFitHeight(100);
		Button drawAChanceCard = new Button("Draw A Card");
		drawAChanceCard.setDisable(true);
		board.add(chanceCardsDeck, 6, 7, 4, 2);
		GridPane.setHalignment(chanceCardsDeck, HPos.CENTER);
		GridPane.setValignment(chanceCardsDeck, VPos.CENTER);
		board.add(drawAChanceCard, 6, 9, 4, 1);
		GridPane.setHalignment(drawAChanceCard, HPos.CENTER);
		GridPane.setValignment(drawAChanceCard, VPos.CENTER);
		ImageView communityChestCardsDeck = new ImageView(new Image("Community Chest Cards/Logo.jpg"));
		communityChestCardsDeck.setPreserveRatio(false);
		communityChestCardsDeck.setFitWidth(200);
		communityChestCardsDeck.setFitHeight(100);
		Button drawACommunityChestCard = new Button("Draw A Card");
		drawACommunityChestCard.setDisable(true);
		board.add(communityChestCardsDeck, 1, 1, 4, 2);
		GridPane.setHalignment(communityChestCardsDeck, HPos.CENTER);
		GridPane.setValignment(communityChestCardsDeck, VPos.CENTER);
		board.add(drawACommunityChestCard, 1, 3, 4, 1);
		GridPane.setHalignment(drawACommunityChestCard, HPos.CENTER);
		GridPane.setValignment(drawACommunityChestCard, VPos.CENTER);
		board.setGridLinesVisible(false);
		Scene gameScene = new Scene(board);
		
		
		// Primary stage: welcomeScene, addAPlayerScene, and gameScene
		primaryStage.setHeight(700);
		primaryStage.setWidth(700);
		primaryStage.setTitle("MONOPOLY");
		primaryStage.setScene(welcomeScene);
		primaryStage.setOnHidden(e -> Platform.exit());
		primaryStage.show();
		
		
		/*******************************
		 * StartTheTurnScene Generation: 
		 * Label: "Hello xx, please start your turn." 
		 * ImageView: Logo
		 * Button: "Start The Turn"
		 */
		Label startTheTurnMessage = new Label();
		startTheTurnMessage.setFont(new Font("Arial", 20));
		startTheTurnMessage.setWrapText(true);
		ImageView startTheTurnLandmark = new ImageView(new Image("Logo/Logo.jpg"));
		startTheTurnLandmark.setPreserveRatio(false);
		startTheTurnLandmark.setFitWidth(300);
		startTheTurnLandmark.setFitHeight(350);
		Button startTheTurnButton = new Button("Start The Turn");
		startTheTurnButton.setFont(new Font("Arial", 20));
		VBox startTheTurnVBox = new VBox(20);
		startTheTurnVBox.getChildren().addAll(startTheTurnMessage, startTheTurnLandmark, startTheTurnButton);
		startTheTurnVBox.setAlignment(Pos.CENTER);
		Scene startTheTurnScene = new Scene(startTheTurnVBox);
		
		
		/*******************************
		 * InJailScene Generation: 
		 * Label: "Hello xx, you are in jail and please select an option to get out of jail."
		 * ImageView: JUST VISITING / IN JAIL
		 * ComboBox: "Roll The Dice", "Pay The Fine of $50", etc.
		 * Button: Confirm to get out of jail
		 * Button: "Next"
		 */
		Label inJailMessage = new Label();
		inJailMessage.setFont(new Font("Arial", 20));
		inJailMessage.setWrapText(true);
		ImageView inJailLandmark = new ImageView(new Image("Landmarks/10.jpg"));
		inJailLandmark.setPreserveRatio(false);
		inJailLandmark.setFitWidth(300);
		inJailLandmark.setFitHeight(350);
		ComboBox<String> inJailComboBox = new ComboBox<>();
		inJailComboBox.setDisable(true);
		Button confirmToGetOutOfJail = new Button("Confirm");
		Button finishInJailAction = new Button("Next");
		confirmToGetOutOfJail.setFont(new Font("Arial", 20));
		finishInJailAction.setFont(new Font("Arial", 20));
		confirmToGetOutOfJail.setDisable(true);
		finishInJailAction.setDisable(true);
		HBox inJailHBox = new HBox(40);
		inJailHBox.getChildren().addAll(confirmToGetOutOfJail, finishInJailAction);
		inJailHBox.setAlignment(Pos.CENTER);
		VBox inJailVBox = new VBox(20);
		inJailVBox.getChildren().addAll(inJailMessage, inJailLandmark, inJailComboBox, inJailHBox);
		inJailVBox.setAlignment(Pos.CENTER);
		Scene inJailScene = new Scene(inJailVBox);
		
		
		/*******************************
		 * RequiredActionScene Generation: 
		 * Label: "Hello xx, you have landed on ...."
		 * ImageView: Corresponding landmark 
		 * Button: "Purchase"
		 * Button: "Auction"
		 * Button: "Next"
		 */
		Label requiredActionMessage = new Label();
		requiredActionMessage.setFont(new Font("Arial", 20));
		requiredActionMessage.setWrapText(true);
		ImageView requiredActionLandmark = new ImageView(new Image("Logo/Logo.jpg"));
		requiredActionLandmark.setPreserveRatio(false);
		requiredActionLandmark.setFitWidth(300);
		requiredActionLandmark.setFitHeight(350);
		Button purchase = new Button("Purchase");
		Button auction = new Button("Auction");
		Button finishRequiredAction = new Button("Next");
		purchase.setFont(new Font("Arial", 20));
		auction.setFont(new Font("Arial", 20));
		finishRequiredAction.setFont(new Font("Arial", 20));
		purchase.setDisable(true);
		auction.setDisable(true);
		HBox requiredActionHBox = new HBox(40);
		requiredActionHBox.getChildren().addAll(purchase, auction, finishRequiredAction);
		requiredActionHBox.setAlignment(Pos.CENTER);
		VBox requiredActionVBox = new VBox(20);
		requiredActionVBox.getChildren().addAll(requiredActionMessage, requiredActionLandmark, requiredActionHBox);
		requiredActionVBox.setAlignment(Pos.CENTER);
		Scene requiredActionScene = new Scene(requiredActionVBox);
		
		
		/*******************************
		 * OptionalActionScene Generation: 
		 * Label: "Hello xx ", please select an option and/or finish the turn." 
		 * ImageView: Logo by default, and current player selection dependent 
		 * ComboBox: "Improve a property", "Mortgage a property", etc. 
		 * ComboBox: Property list depending on the last ComboBox selection
		 * Button: "Confirm The Action"
		 * Button: "Finish The Turn"
		 */
		Label optionalActionMessage = new Label();
		optionalActionMessage.setFont(new Font("Arial", 20));
		optionalActionMessage.setWrapText(true);
		ImageView optionalActionLandmark = new ImageView(new Image("Logo/Logo.jpg"));
		optionalActionLandmark.setPreserveRatio(false);
		optionalActionLandmark.setFitWidth(300);
		optionalActionLandmark.setFitHeight(350);
		ComboBox<String> optionalActionComboBox = new ComboBox<>();
		ComboBox<String> optionComboBox = new ComboBox<>();
		optionalActionComboBox.setDisable(true);
		optionComboBox.setDisable(true);
		Button confirmTheAction = new Button("Confirm The Action");
		Button finishTheTurn = new Button("Finish The Turn");
		confirmTheAction.setFont(new Font("Arial", 20));
		finishTheTurn.setFont(new Font("Arial", 20));
		confirmTheAction.setDisable(true);
		HBox optionalActionHBox = new HBox(40);
		optionalActionHBox.getChildren().addAll(confirmTheAction, finishTheTurn);
		optionalActionHBox.setAlignment(Pos.CENTER);
		VBox optionalActionVBox = new VBox(20);
		optionalActionVBox.getChildren().addAll(optionalActionMessage, optionalActionLandmark, optionalActionComboBox, optionComboBox, optionalActionHBox);
		optionalActionVBox.setAlignment(Pos.CENTER);
		Scene optionalActionScene = new Scene(optionalActionVBox);
		
		
		// Secondary stage: startTheTurnScene, inJailScene, requiredActionScene, optionalActionScene
		Stage secondaryStage = new Stage();
		secondaryStage.setHeight(600);
		secondaryStage.setWidth(600);
		secondaryStage.setTitle("ACTIONS");
		secondaryStage.setScene(startTheTurnScene);
		secondaryStage.close();
		
		
		/*******************************
		 * ChanceCardScene Generation: 
		 * Label: "Hello xx, please follow instructions on the card." 
		 * ImageView: Chance card logo by default, and top card dependent 
		 * Button: keep the chance card (out of jail)
		 * Button: "Next"
		 */
		Label chanceCardMessage = new Label();
		chanceCardMessage.setFont(new Font("Arial", 20));
		chanceCardMessage.setWrapText(true);
		ImageView chanceCard = new ImageView(new Image("Chance Cards/Logo.jpg"));
		chanceCard.setPreserveRatio(false);
		chanceCard.setFitWidth(300);
		chanceCard.setFitHeight(350);
		Button keepTheChanceCard = new Button("Keep");
		Button finishChanceCardAction = new Button("Next");
		keepTheChanceCard.setFont(new Font("Arial", 20));
		finishChanceCardAction.setFont(new Font("Arial", 20));
		keepTheChanceCard.setDisable(true);
		HBox chanceCardHBox = new HBox(40);
		chanceCardHBox.getChildren().addAll(keepTheChanceCard, finishChanceCardAction);
		chanceCardHBox.setAlignment(Pos.CENTER);
		VBox chanceCardVBox = new VBox(20);
		chanceCardVBox.getChildren().addAll(chanceCardMessage, chanceCard, chanceCardHBox);
		chanceCardVBox.setAlignment(Pos.CENTER);
		Scene chanceCardScene = new Scene(chanceCardVBox);
		
		
		/*******************************
		 * CommunityChestCardScene Generation: 
		 * Label: "Hello xx, please follow instructions on the card." 
		 * ImageView: Community chest card logo by default, and top card dependent 
		 * Button: keep the community chest card (out of jail)
		 * Button: "Next"
		 */
		Label communityChestCardMessage = new Label();
		communityChestCardMessage.setFont(new Font("Arial", 20));
		communityChestCardMessage.setWrapText(true);
		ImageView communityChestCard = new ImageView(new Image("Community Chest Cards/Logo.jpg"));
		communityChestCard.setPreserveRatio(false);
		communityChestCard.setFitWidth(300);
		communityChestCard.setFitHeight(350);
		Button keepTheCommunityChestCard = new Button("Keep");
		Button finishCommunityChestCardAction = new Button("Next");
		keepTheCommunityChestCard.setFont(new Font("Arial", 20));
		finishCommunityChestCardAction.setFont(new Font("Arial", 20));
		keepTheCommunityChestCard.setDisable(true);
		HBox communityChestCardHBox = new HBox(40);
		communityChestCardHBox.getChildren().addAll(keepTheCommunityChestCard, finishCommunityChestCardAction);
		communityChestCardHBox.setAlignment(Pos.CENTER);
		VBox communityChestCardVBox = new VBox(20);
		communityChestCardVBox.getChildren().addAll(communityChestCardMessage, communityChestCard, communityChestCardHBox);
		communityChestCardVBox.setAlignment(Pos.CENTER);
		Scene communityChestCardScene = new Scene(communityChestCardVBox);
		
		
		// Tertiary stage: chanceCardScene, communityChestCardScene
		Stage tertiaryStage = new Stage();
		tertiaryStage.setHeight(600);
		tertiaryStage.setWidth(600);
		tertiaryStage.close();
		
		
		/*******************************
		 * When startTheGame is clicked, 
		 * Add all input players, 
		 * Primary stage: gameScene with board and tokens 
		 * Secondary stage: "Hello xx, please start your turn." 
		 */
		startTheGame.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	for(Player aPlayer: players) {
		    		board.add(aPlayer.getToken(), 10, 10);
		    		setOrientation(aPlayer, aPlayer.getID());
		    	}
		    	primaryStage.setScene(gameScene);
		    	startTheTurnMessage.setText("Hello " + players.get(0).getUsername() + ", please start your turn.");
		    	secondaryStage.setScene(startTheTurnScene);
				secondaryStage.show();
		    }
		});
		
		
		/*******************************
		 * When addAPlayer is clicked, 
		 * Primary stage: addAPlayerScene
		 */
		addAPlayer.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	primaryStage.setScene(addAPlayerScene);
		    }
		});
		
		
		/*******************************
		 * When a token is selected 
		 * Activate confirmAddingAPlayer button 
		 */
		group.selectedToggleProperty().addListener((observableToggles, previousToggle, currentToggle) -> {
		    if (currentToggle.isSelected()) {
		    	confirmAddingAPlayer.setDisable(false);
		    }
		});
		
		
		/*******************************
		 * When confirmAddingAPlayer is clicked, 
		 * Initialize a player with input username, id, and token 
		 * Clear up username text field 
		 * Disable confirmAddingAPlayer button 
		 * If the number of players reaches two, activate startTheGame button 
		 * If the number of players reaches the maximum of eight, disable addAPlayer button 
		 * Primary stage: welcomeScene
		 */
		confirmAddingAPlayer.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	players.add(new Player(usernameField.getText(), players.size(), ((ImageView) ((RadioButton) group.getSelectedToggle()).getGraphic())));
		    	((RadioButton) group.getSelectedToggle()).setDisable(true);
		    	usernameField.clear();
		    	confirmAddingAPlayer.setDisable(true);
		    	if (players.size() > 1) {
		    		startTheGame.setDisable(false);
		    	}
		    	if (players.size() == 8) {
		    		addAPlayer.setDisable(true);
		    	}
		    	primaryStage.setScene(welcomeScene);
		    }
		});
		
		
		/*******************************
		 * When cancelAddingAPlayer is clicked, 
		 * Primary stage: welcomeScene
		 */
		cancelAddingAPlayer.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	primaryStage.setScene(welcomeScene);
		    }
		});
		
		
		/*******************************
		 * PlayerStatusLabel is updated every 0.1s
		 */
		Timeline playerStatusUpdater = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playerStatusLabel.setText("");
	    		for(Player aPlayer: players) {
		    		playerStatusLabel.setText(playerStatusLabel.getText() + aPlayer.getUsername() + ": " + aPlayer.getMoney() + "\n");
		    	}
		    }
		}));
		playerStatusUpdater.setCycleCount(Timeline.INDEFINITE);
		playerStatusUpdater.play();
		
		
		/*******************************
		 * When startTheTurnButton is clicked, 
		 * Activate rollTheDice button 
		 * "Hello, xx, please roll the dice."
		 */
		startTheTurnButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	rollTheDice.setDisable(false);
		    	startTheTurnMessage.setText("Hello " + players.get(players.get(0).getCurrentPlayerIndex()).getUsername() + ", please roll the dice.");
		    }
		});
		
		
		/*******************************
		 * When rollTheDice is clicked, 
		 * Update dice 
		 * Disable rollTheDice button 
		 * If the current player is in jail and fails to roll a double, then stay in jail 
		 * If the current player is not in jail and rolls three doubles, then go to jail 
		 * If the current player is in jail and rolls a double, then get out of jail 
		 * If the current player is in jail and fails to roll a double in three turns, then pay the fine to get out of jail 
		 * Further action: depending on the position and owner
		 */
		rollTheDice.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {		    	
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	dice1.setNumber();
		    	dice2.setNumber();
		    	if(dice1.getNumber() == dice2.getNumber() && dice1.getToBeExtraRolled() == false) {
		    		dice1.addDoubleNumber();
		    	}
		    	dice1.setFaceIcon();
		    	dice2.setFaceIcon();
		    	board.add(dice1.getFaceIcon(), 1, 7, 2, 2);
		        GridPane.setHalignment(dice1.getFaceIcon(), HPos.CENTER);
				GridPane.setValignment(dice1.getFaceIcon(), VPos.CENTER);
		        board.add(dice2.getFaceIcon(), 3, 7, 2, 2);
		        GridPane.setHalignment(dice2.getFaceIcon(), HPos.CENTER);
				GridPane.setValignment(dice2.getFaceIcon(), VPos.CENTER);
		    	rollTheDice.setDisable(true);
		    	if((currentPlayer.getInJailNumber() == 0 || currentPlayer.getInJailNumber() == 1) && dice1.getNumber() != dice2.getNumber()) {
		    		currentPlayer.addInJailNumber();
		    		inJailMessage.setText("Hello " + currentPlayer.getUsername() + ", you have to wait for next turn to get out of jail. Please click on the Next button.");
	    			finishInJailAction.setDisable(false);
		    	}
		    	else if(currentPlayer.getInJailNumber() == -1 && dice1.getDoubleNumber() == 3) {
		    		currentPlayer.addInJailNumber();
		    		currentPlayer.setPosition(10);
	    			board.getChildren().remove(currentPlayer.getToken());
					board.add(currentPlayer.getToken(), indexToSubscript(currentPlayer.getPosition())[0], indexToSubscript(currentPlayer.getPosition())[1]);
					setOrientation(currentPlayer, currentPlayer.getID());
	    			requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", you have rolled three doubles and been sent to jail. Please click on the Next button.");
	    			requiredActionLandmark.setImage(new Image("Landmarks/" + currentPlayer.getPosition() + ".jpg"));
	    			secondaryStage.setScene(requiredActionScene);
		    	}
		    	else {
		    		if(dice1.getToBeExtraRolled() == false) {
		    			currentPlayer.moveForward(dice1.getNumber() + dice2.getNumber());
		    			board.getChildren().remove(currentPlayer.getToken());
						board.add(currentPlayer.getToken(), indexToSubscript(currentPlayer.getPosition())[0], indexToSubscript(currentPlayer.getPosition())[1]);
						setOrientation(currentPlayer, currentPlayer.getID());
		    		}
		    		Landmark currentLandmark = route.get(currentPlayer.getPosition());
					requiredActionLandmark.setImage(new Image("Landmarks/" + currentPlayer.getPosition() + ".jpg"));
					if(currentPlayer.getInJailNumber() == 0 || currentPlayer.getInJailNumber() == 1) {
						currentPlayer.setInJailNumber();
						currentPlayer.setJustOutOfJail(true);
						requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", you have rolled a double and been out of jail now, and ");
		    		}
		    		else if(currentPlayer.getInJailNumber() == 2) {
		    			currentPlayer.reduceMoney(50);
		    			currentPlayer.setInJailNumber();
		    			currentPlayer.setJustOutOfJail(true);
		    			requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", you have paid a fine of $50 and been out of jail now, and ");
		    		}
		    		else {
		    			requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", ");
		    		}
		    		switch(currentPlayer.getPosition()) {
		    		case 0:
		    		case 10:
		    		case 20:
		    			requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + ". Please click on the Next button.");
		    			break;
		    		case 7:
		    		case 22:
		    		case 36:
		    			drawAChanceCard.setDisable(false);
		    			requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + ". Please draw a chance card.");
		    			finishRequiredAction.setDisable(true);
		    			break;
		    		case 2:
		    		case 17:
		    		case 33:
		    			drawACommunityChestCard.setDisable(false);
		    			requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + ". Please draw a community chest card.");
		    			finishRequiredAction.setDisable(true);
		    			break;
		    		case 30:
		    			requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + ", and been sent to jail. Please click on the Next button.");
		    			currentPlayer.addInJailNumber();
		    			currentPlayer.setPosition(10);
		    			board.getChildren().remove(currentPlayer.getToken());
						board.add(currentPlayer.getToken(), indexToSubscript(currentPlayer.getPosition())[0], indexToSubscript(currentPlayer.getPosition())[1]);
						setOrientation(currentPlayer, currentPlayer.getID());
		    			break;
		    		case 4:
		    		case 38:
		    			currentPlayer.reduceMoney(((TaxSpace) currentLandmark).getPenalties());
		    			requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + ", and paid the labeled tax. Please click on the Next button.");
		    			break;
		    		default:
		    			if(currentLandmark.getOwnerID() == -1) {
		    				requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on an unowned property " + currentLandmark.getDescription() + ".");
			    			purchase.setDisable(false);
		    			}
		    			else if(currentLandmark.getOwnerID() != currentPlayer.getID()) {
		    				for(Player aPlayer: players) {
		    					if(currentLandmark.getOwnerID() == aPlayer.getID()) {
		    						if(currentLandmark.getMortgaged()) {
		    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on a mortgaged property " + currentLandmark.getDescription() + " owned by " + aPlayer.getUsername() + ", and do not need to pay the rent. Please click on the Next button.");
		    						}
		    						else if(currentLandmark instanceof Railroad) {
		    							Railroad tempRailroad = (Railroad) currentLandmark;
		    							ArrayList<Integer> tempRailroadIDs = aPlayer.getPropertyIDs();
		    							tempRailroadIDs.retainAll(getArrayList(tempRailroad.getGroupLocations(currentPlayer.getPosition())));
		    							int tempRent = tempRailroad.getRents() [tempRailroadIDs.size() - 1];
		    							currentPlayer.reduceMoney(tempRent);
		    							aPlayer.addMoney(tempRent);
		    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + tempRailroad.getDescription() + " owned by " + aPlayer.getUsername() + " who have " + tempRailroadIDs.size() + " railroad(s), and paid $" + tempRent + ". Please click on the Next button.");
		    						}	
		    						else if(currentLandmark instanceof ColorGroup) {
		    							ColorGroup tempColorGroup = (ColorGroup) currentLandmark;
		    							int tempRent = tempColorGroup.getRents() [tempColorGroup.getBuildingNumber()];
		    							if(tempColorGroup.getBuildingNumber() == 0 && tempColorGroup.getGroupOwnerID(currentPlayer.getPosition()) == currentPlayer.getID()) {
		    								tempRent *= 2;
		    							}
		    							currentPlayer.reduceMoney(tempRent);
		    							aPlayer.addMoney(tempRent);
		    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + tempColorGroup.getDescription() + " with " + (tempColorGroup.getBuildingNumber() == 5? "a hotel" : tempColorGroup.getBuildingNumber() + " house(s)") + " owned by " + aPlayer.getUsername() + ", and paid $" + tempRent + ". Please click on the Next button.");
		    						}
		    						else {
		    							Utility tempUtility = (Utility) currentLandmark;
		    							ArrayList<Integer> tempUtilityIDs = aPlayer.getPropertyIDs();
		    							tempUtilityIDs.retainAll(getArrayList(tempUtility.getGroupLocations(currentPlayer.getPosition())));
		    							int tempRent = ((tempUtilityIDs.size() == 1 && dice1.getToBeExtraRolled() == false)? 4 * (dice1.getNumber() + dice2.getNumber()) : 10 * (dice1.getNumber() + dice2.getNumber()));
		    							currentPlayer.reduceMoney(tempRent);
		    							aPlayer.addMoney(tempRent);
		    							if(dice1.getToBeExtraRolled()) {
		    								finishRequiredAction.setDisable(false);
		    							}
		    							dice1.setToBeExtraRolled(false);
		    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + tempUtility.getDescription() + " owned by " + aPlayer.getUsername() + " who have " + (tempUtilityIDs.size() == 1? "1 utility" : "2 utilities") + ", and paid $" + tempRent + ". Please click on the Next button.");
		    						}
		    						break;
		    					}
		    				}
		    				requiredActionLandmark.setImage(new Image("Properties/" + currentPlayer.getPosition() + ".jpg"));
		    			}
		    			else {
		    				requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + " owned by yourself. Please click on the Next button.");
		    			}
		    			break;
		    		}
		    		secondaryStage.setScene(requiredActionScene);
		    	}	
		    }
		});
		
		
		/*******************************
		 * When a choice of inJailComboBox is selected, 
		 * Activate confirmToGetOutOfJail button 
		 */
		inJailComboBox.getSelectionModel().selectedItemProperty().addListener((observableItems, previousItem, currentItem) -> {
			if(inJailComboBox.getValue() != null) {
				confirmToGetOutOfJail.setDisable(false);
			}
		});
		
		
		/*******************************
		 * When confirmToGetOutOfJail is clicked, 
		 * Take actions based on the selection of inJailComboBox 
		 */
		confirmToGetOutOfJail.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	if(inJailComboBox.getValue().equals("Pay The Fine of $50")) {
		    		currentPlayer.reduceMoney(50);
		    		currentPlayer.setInJailNumber();
			    	currentPlayer.setJustOutOfJail(true);
			    	inJailMessage.setText("Hello " + currentPlayer.getUsername() + ", you have paid a fine of $50 and been out of jail now. Please roll the dice.");
		    	}
		    	else if(inJailComboBox.getValue().equals("Use Get Out Of Jail Free Chance Card")) {
		    		currentPlayer.setInJailNumber();
			    	currentPlayer.setJustOutOfJail(true);
			    	currentPlayer.setOutOfJailCOwnerID(-1);
			    	chanceCardIDs.add(8);
			    	inJailMessage.setText("Hello " + currentPlayer.getUsername() + ", you have used and returned Get Out Of Jail Free chance card. Please roll the dice.");
		    	}
		    	else if(inJailComboBox.getValue().equals("Use Get Out Of Jail Free Community Chest Card")) {
		    		currentPlayer.setInJailNumber();
			    	currentPlayer.setJustOutOfJail(true);
			    	currentPlayer.setOutOfJailCCOwnerID(-1);
			    	communityChestCardIDs.add(5);
			    	inJailMessage.setText("Hello " + currentPlayer.getUsername() + ", you have used and returned Get Out Of Jail Free community chest card. Please roll the dice.");
		    	}
		    	else {
		    		inJailMessage.setText("Hello " + currentPlayer.getUsername() + ", please roll the dice.");
			    	
		    	}
		    	rollTheDice.setDisable(false);
		    	inJailComboBox.setDisable(true);
		    	confirmToGetOutOfJail.setDisable(true);
		    }
		});
		
		
		/*******************************
		 * When finishInJailAction is clicked, 
		 * Disable finishInJailAction button 
		 * "Hello xx, please click on the Next button again." 
		 * Landmark the current player lands on 
		 */
		finishInJailAction.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	finishInJailAction.setDisable(true);
		    	requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", please click on the Next button again.");
		    	requiredActionLandmark.setImage(new Image("Landmarks/" + currentPlayer.getPosition() + ".jpg"));
		    	secondaryStage.setScene(requiredActionScene);
		    }
		});
		
		
		/*******************************
		 * When purchase is clicked, 
		 * Reduce money based on the price 
		 * Add the property ID in the list the current player owns 
		 * Update the property owner and possibly the color zone owner 
		 * "Hello xx, you have purchased xx" 
		 * Disable purchase button 
		 */
		purchase.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	Landmark tempLandmark = route.get(currentPlayer.getPosition());
		    	currentPlayer.reduceMoney(tempLandmark.getPrice());
		    	currentPlayer.addPropertyID(currentPlayer.getPosition());
		    	tempLandmark.setOwnerID(currentPlayer.getID());
		    	if(getArrayList(tempLandmark.getGroupLocations(currentPlayer.getPosition())).stream().filter(tempindex -> route.get(tempindex).getOwnerID() != currentPlayer.getID()).collect(Collectors.toList()).size() == 0) {
		    		tempLandmark.setGroupOwnerID(currentPlayer.getPosition(), currentPlayer.getID());
		    	};
		    	requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", you have purchased " + tempLandmark.getDescription() + ". Please click on the Next button.");
		    	purchase.setDisable(true);
		    }
		});
		
		
		/*******************************
		 * When finishRequiredAction is clicked, 
		 * Disable purchase button 
		 * Move on to optionalActionScene
		 * "Hello xx, please select an option and/or finish the turn." 
		 */
		finishRequiredAction.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	purchase.setDisable(true);
		    	optionalActionMessage.setText("Hello " + players.get(players.get(0).getCurrentPlayerIndex()).getUsername() + ", please select an option and/or finish the turn.");
		    	secondaryStage.setScene(optionalActionScene);
		    }
		});
		
		
		/*******************************
		 * When drawAChanceCard is clicked, 
		 * Take actions based on the instructions of the top card 
		 * Update token position 
		 * Disable drawAChanceCard button 
		 * Show the top card on tertiary stage 
		 */
		drawAChanceCard.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	switch(chanceCardIDs.get(0)) {
		    	case 1:
		    		currentPlayer.setPosition(0);
		    		break;
		    	case 2:
		    		currentPlayer.setPosition(24);
		    		break;
		    	case 3:
		    		currentPlayer.setPosition(11);
		    		break;
		    	case 4:
		    	case 5:
		    		if(currentPlayer.getPosition() == 7) {
		    			currentPlayer.setPosition(15);
		    		}
		    		else if(currentPlayer.getPosition() == 22) {
		    			currentPlayer.setPosition(25);
		    		}
		    		else {
		    			currentPlayer.setPosition(5);
		    		}
		    		break;
		    	case 6:
		    		if(currentPlayer.getPosition() == 22) {
		    			currentPlayer.setPosition(28);
		    		}
		    		else {
		    			currentPlayer.setPosition(12);
		    		}
		    		break;
		    	case 7:
		    		currentPlayer.addMoney(50);
		    		break;
		    	case 8:
		    		keepTheChanceCard.setDisable(false);
		    		break;
		    	case 9:
		    		currentPlayer.moveBackward(3);
		    		break;
		    	case 10:
		    		currentPlayer.addInJailNumber();
		    		currentPlayer.setPosition(10);
		    		break;
		    	case 11:
		    		for(int propertyID: currentPlayer.getPropertyIDs()) {
		    			if(route.get(propertyID) instanceof ColorGroup) {
		    				if(((ColorGroup) route.get(propertyID)).getBuildingNumber() < 5) {
		    					currentPlayer.reduceMoney(((ColorGroup) route.get(propertyID)).getBuildingNumber() * 25);
		    				}
		    				else {
		    					currentPlayer.reduceMoney(100);
		    				}
		    			}
		    		}
		    		break;
		    	case 12:
		    		currentPlayer.reduceMoney(15);
		    		break;
		    	case 13:
		    		currentPlayer.setPosition(5);
		    		break;
		    	case 14:
		    		currentPlayer.setPosition(39);
		    		break;
		    	case 15:
		    		currentPlayer.addMoney(150);
		    		break;
		    	case 16:
		    		for(Player aPlayer: players) {
		    			currentPlayer.reduceMoney(50);
		    			aPlayer.addMoney(50);
		    		}
		    		break;
		    	default:
		    		break;
		    	}
		    	board.getChildren().remove(currentPlayer.getToken());
				board.add(currentPlayer.getToken(), indexToSubscript(currentPlayer.getPosition())[0], indexToSubscript(currentPlayer.getPosition())[1]);
				setOrientation(currentPlayer, currentPlayer.getID());
				drawAChanceCard.setDisable(true);
				chanceCardMessage.setText("Hello " + currentPlayer.getUsername() + ", please follow instructions on the card.");
		    	chanceCard.setImage(new Image("Chance Cards/" + chanceCardIDs.get(0) + ".jpg"));
		    	tertiaryStage.setTitle("CHNACE CARD");
		    	tertiaryStage.setScene(chanceCardScene);
		    	tertiaryStage.show();
		    }
		});
		
		
		/*******************************
		 * When keepTheChanceCard is clicked, 
		 * Assign current player id to the card (out of jail) owner 
		 * Remove the card from the chance card deck 
		 * Disable keepTheChanceCard button 
		 * Close tertiary stage 
		 * "Hello xx, please click on the Next button."
		 */
		keepTheChanceCard.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	currentPlayer.setOutOfJailCOwnerID(currentPlayer.getID());
	    		chanceCardIDs.remove(0);
	    		keepTheChanceCard.setDisable(true);
		    	tertiaryStage.close();
		    	requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", please click on the Next button.");
		    	finishRequiredAction.setDisable(false);
		    }
		});
		
		
		/*******************************
		 * When finishChanceCardAction is clicked, 
		 * Take further actions at a possible new position caused by the card action 
		 * Move the card from the top to the bottom of chance card deck
		 * Disable keepTheChanceCard button 
		 * Close tertiary stage 
		 */
		finishChanceCardAction.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	Landmark currentLandmark = route.get(currentPlayer.getPosition());
		    	requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", ");
		    	requiredActionLandmark.setImage(new Image("Landmarks/" + currentPlayer.getPosition() + ".jpg"));
		    	finishRequiredAction.setDisable(false);
		    	switch(currentPlayer.getPosition()) {
		    	case 4:
		    		currentPlayer.reduceMoney(200);
		    		requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + ", and paid the labeled tax. Please click on the Next button.");
		    		break;
		    	case 33:
		    		drawACommunityChestCard.setDisable(false);
	    			requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + ". Please draw a community chest card.");
	    			finishRequiredAction.setDisable(true);
	    			break;
		    	case 5:
		    	case 11:
		    	case 12:
		    	case 15:
		    	case 19:
		    	case 24:
	    		case 25:
	    		case 28:
	    		case 39:
	    			if(currentLandmark.getOwnerID() == -1) {
	    				requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on an unowned property " + currentLandmark.getDescription() + ".");
		    			purchase.setDisable(false);
	    			}
	    			else if(currentLandmark.getOwnerID() != currentPlayer.getID()) {
	    				for(Player aPlayer: players) {
	    					if(currentLandmark.getOwnerID() == aPlayer.getID()) {
	    						if(currentLandmark.getMortgaged()) {
	    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on a mortgaged property " + currentLandmark.getDescription() + " owned by " + aPlayer.getUsername() + ", and do not need to pay the rent. Please click on the Next button.");
	    						}
	    						else if(currentLandmark instanceof Railroad) {
	    							Railroad tempRailroad = (Railroad) currentLandmark;
	    							ArrayList<Integer> tempRailroadIDs = aPlayer.getPropertyIDs();
	    							tempRailroadIDs.retainAll(getArrayList(tempRailroad.getGroupLocations(currentPlayer.getPosition())));
	    							int tempRent = (chanceCardIDs.get(0) == 13? tempRailroad.getRents() [tempRailroadIDs.size() - 1] : tempRailroad.getRents() [tempRailroadIDs.size() - 1] * 2);
	    							currentPlayer.reduceMoney(tempRent);
	    							aPlayer.addMoney(tempRent);
	    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + tempRailroad.getDescription() + " owned by " + aPlayer.getUsername() + " who have " + tempRailroadIDs.size() + " railroad(s), and paid $" + tempRent + ". Please click on the Next button.");
	    						}	
	    						else if(currentLandmark instanceof ColorGroup) {
	    							ColorGroup tempColorGroup = (ColorGroup) currentLandmark;
	    							int tempRent = tempColorGroup.getRents() [tempColorGroup.getBuildingNumber()];
	    							if(tempColorGroup.getBuildingNumber() == 0 && tempColorGroup.getGroupOwnerID(currentPlayer.getPosition()) == currentPlayer.getID()) {
	    								tempRent *= 2;
	    							}
	    							currentPlayer.reduceMoney(tempRent);
	    							aPlayer.addMoney(tempRent);
	    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + tempColorGroup.getDescription() + " with " + (tempColorGroup.getBuildingNumber() == 5? "a hotel" : tempColorGroup.getBuildingNumber() + " house(s)") + " owned by " + aPlayer.getUsername() + ", and paid $" + tempRent + ". Please click on the Next button.");
	    						}
	    						else {
	    							dice1.setToBeExtraRolled(true);
	    							rollTheDice.setDisable(false);
	    							requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + " owned by " + aPlayer.getUsername() + ". Please roll the dice.");
	    							finishRequiredAction.setDisable(true);
	    						}
	    						break;
	    					}
	    				}
	    				requiredActionLandmark.setImage(new Image("Properties/" + currentPlayer.getPosition() + ".jpg"));
	    			}
	    			else {
	    				requiredActionMessage.setText(requiredActionMessage.getText() + "you have landed on " + currentLandmark.getDescription() + " owned by yourself. Please click on the Next button.");
	    			}
	    			break;
	    		default:
	    			requiredActionMessage.setText(requiredActionMessage.getText() + "please click on the Next button.");
		    		break;
	    		}
	    		chanceCardIDs.add(chanceCardIDs.get(0));
	    		chanceCardIDs.remove(0);
	    		keepTheChanceCard.setDisable(true);
		    	tertiaryStage.close();
		    }
		});
		
		
		/*******************************
		 * When drawACommunityChestCard is clicked, 
		 * Take actions based on the instructions of the top card 
		 * Update token position 
		 * Disable drawACommunityChestCard button 
		 * Show the top card on tertiary stage 
		 */
		drawACommunityChestCard.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	switch(communityChestCardIDs.get(0)) {
		    	case 1:
		    		currentPlayer.setPosition(0);
		    		break;
		    	case 2:
		    		currentPlayer.addMoney(200);
		    		break;
		    	case 3:
		    		currentPlayer.reduceMoney(50);
		    		break;
		    	case 4:
		    		currentPlayer.addMoney(50);
		    		break;
		    	case 5:
		    		keepTheCommunityChestCard.setDisable(false);
		    		break;
		    	case 6:
		    		currentPlayer.addInJailNumber();
		    		currentPlayer.setPosition(10);
		    		break;
		    	case 7:
		    		currentPlayer.addMoney(20);
		    		break;
		    	case 8:
		    		for(Player aPlayer: players) {
		    			aPlayer.reduceMoney(10);
		    			currentPlayer.addMoney(10);
		    		}
		    		break;
		    	case 9:
		    		currentPlayer.addMoney(100);
		    		break;
		    	case 10:
		    		currentPlayer.reduceMoney(100);
		    		break;
		    	case 11:
		    		currentPlayer.reduceMoney(50);
		    		break;
		    	case 12:
		    		currentPlayer.addMoney(25);
		    		break;
		    	case 13:
		    		for(int propertyID: currentPlayer.getPropertyIDs()) {
		    			if(route.get(propertyID) instanceof ColorGroup) {
		    				if(((ColorGroup) route.get(propertyID)).getBuildingNumber() < 5) {
		    					currentPlayer.reduceMoney(((ColorGroup) route.get(propertyID)).getBuildingNumber() * 40);
		    				}
		    				else {
		    					currentPlayer.reduceMoney(115);
		    				}
		    			}
		    		}
		    		break;
		    	case 14:
		    		currentPlayer.addMoney(10);
		    		break;
		    	case 15:
		    		currentPlayer.addMoney(100);
		    		break;
		    	case 16:
		    		currentPlayer.addMoney(100);
		    		break;
		    	default:
		    		break;
		    	}
		    	board.getChildren().remove(currentPlayer.getToken());
				board.add(currentPlayer.getToken(), indexToSubscript(currentPlayer.getPosition())[0], indexToSubscript(currentPlayer.getPosition())[1]);
				setOrientation(currentPlayer, currentPlayer.getID());
		    	drawACommunityChestCard.setDisable(true);
		    	communityChestCardMessage.setText("Hello " + currentPlayer.getUsername() + ", please follow instructions on the card.");
		    	communityChestCard.setImage(new Image("Community Chest Cards/" + communityChestCardIDs.get(0) + ".jpg"));
		    	tertiaryStage.setTitle("COMMUNITY CHEST CARD");
		    	tertiaryStage.setScene(communityChestCardScene);
		    	tertiaryStage.show();
		    }
		});
		
		
		/*******************************
		 * When keepTheCommunityChestCard is clicked, 
		 * Assign current player id to the card (out of jail) owner 
		 * Remove the card from the community chest card deck 
		 * Disable keepTheCommunityChestCard button 
		 * Close tertiary stage 
		 * "Hello xx, please click on the Next button."
		 */
		keepTheCommunityChestCard.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    	currentPlayer.setOutOfJailCCOwnerID(currentPlayer.getID());
	    		communityChestCardIDs.remove(0);
	    		keepTheCommunityChestCard.setDisable(true);
		    	tertiaryStage.close();
		    	requiredActionMessage.setText("Hello " + currentPlayer.getUsername() + ", please click on the Next button.");
		    	finishRequiredAction.setDisable(false);
		    }
		});
		
		
		/*******************************
		 * When finishCommunityChestCardAction is clicked, 
		 * Move the card from the top to the bottom of community chest card deck
		 * Disable finishCommunityChestCardAction button 
		 * Close tertiary stage 
		 */
		finishCommunityChestCardAction.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	communityChestCardIDs.add(communityChestCardIDs.get(0));
	    		communityChestCardIDs.remove(0);
	    		keepTheCommunityChestCard.setDisable(true);
		    	tertiaryStage.close();
		    	requiredActionMessage.setText("Hello " + players.get(players.get(0).getCurrentPlayerIndex()).getUsername() + ", please click on the Next button.");
		    	requiredActionLandmark.setImage(new Image("Landmarks/" + players.get(players.get(0).getCurrentPlayerIndex()).getPosition() + ".jpg"));
		    	finishRequiredAction.setDisable(false);
		    }
		});
		
		
		/*******************************
		 * To be added
		 */
		confirmTheAction.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	
		    	
		    	
		    }
		});
		
		
		/*******************************
		 * When finishTheTurn is clicked, 
		 * If the current player rolls a double, then go back to startTheTurnScene 
		 * Otherwise the next player goes to inJailScene or startTheTurnScene depending on the status 
		 */
		finishTheTurn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	if(players.get(players.get(0).getCurrentPlayerIndex()).getInJailNumber() == -1 && players.get(players.get(0).getCurrentPlayerIndex()).getJustOutOfJail() == false && dice1.getNumber() == dice2.getNumber()) {
		    		startTheTurnMessage.setText("Hello " + players.get(players.get(0).getCurrentPlayerIndex()).getUsername() + ", please start your turn.");
			    	secondaryStage.setScene(startTheTurnScene);
		    	}
		    	else {
		    		players.get(0).setNextPlayerIndex(players.size());
		    		Player currentPlayer = players.get(players.get(0).getCurrentPlayerIndex());
		    		currentPlayer.setJustOutOfJail(false);
			    	dice1.setDoubleNumber();
			    	if(currentPlayer.getInJailNumber() > -1) {
			    		inJailMessage.setText("Hello " + currentPlayer.getUsername() + ", you are in jail and please select an option to get out of jail.");
			    		inJailComboBox.setDisable(false);
			    		inJailComboBox.getItems().clear();
			    		inJailComboBox.getItems().add("Roll The Dice");
			    		if(currentPlayer.getMoney() >= 50) {
			    			inJailComboBox.getItems().add("Pay The Fine of $50");
			    		}
			    		if(currentPlayer.getOutOfJailCOwnerID() == currentPlayer.getID()) {
			    			inJailComboBox.getItems().add("Use Get Out Of Jail Free Chance Card");
			    		}
//			    		else if(currentPlayer.getOutOfJailCOwnerID() != -1) {
//			    			inJailComboBox.getItems().add("Buy Get Out Of Jail Free Chance Card");
//			    		}
			    		if(currentPlayer.getOutOfJailCCOwnerID() == currentPlayer.getID()) {
			    			inJailComboBox.getItems().add("Use Get Out Of Jail Free Community Chest Card");
			    		}
//			    		else if(currentPlayer.getOutOfJailCCOwnerID() != -1) {
//			    			inJailComboBox.getItems().add("Buy Get Out Of Jail Free Community Chest Card");
//			    		}			    		
		    			secondaryStage.setScene(inJailScene);
			    	}
			    	else {
			    		startTheTurnMessage.setText("Hello " + currentPlayer.getUsername() + ", please start your turn.");
				    	secondaryStage.setScene(startTheTurnScene);
			    	}
		    	}
		    }
		});
	}
	
	
	/*******************************
	 * Generate a landmark with description, color, price, rents, etc. 
	 */
	public Landmark getLandmark(int anIndex) {
		Landmark tempLandmark = new Landmark("", Color.GRAY);
		switch(anIndex) {
		case 0:
			tempLandmark = new Landmark("GO", Color.GRAY);
			break;
		case 1:
			tempLandmark = new ColorGroup("MEDITERRANEAN AVENUE", Color.BROWN, 60, 50, new int[] {2, 10, 30, 90, 160, 250});
			break;
		case 2:
			tempLandmark = new Landmark("COMMUNITY CHEST", Color.GRAY);
			break;
		case 3:
			tempLandmark = new ColorGroup("BALTIC AVENUE", Color.BROWN, 60, 50, new int[] {4, 20, 60, 180, 320, 450});
			break;
		case 4:
			tempLandmark = new TaxSpace("INCOME TAX", Color.GRAY, 200);
			break;
		case 5:
			tempLandmark = new Railroad("READING RAILROAD", Color.GRAY);
			break;
		case 6:
			tempLandmark = new ColorGroup("ORIENTAL AVENUE", Color.LIGHTBLUE, 100, 50, new int[] {6, 30, 90, 270, 400, 550});
			break;
		case 7:
			tempLandmark = new Landmark("CHANCE", Color.GRAY);
			break;
		case 8:
			tempLandmark = new ColorGroup("VERMONT AVENUE", Color.LIGHTBLUE, 100, 50, new int[] {6, 30, 90, 270, 400, 550});
			break;
		case 9:
			tempLandmark = new ColorGroup("CONNECTICUT AVENUE", Color.LIGHTBLUE, 120, 50, new int[] {8, 40, 100, 300, 450, 600});
			break;
		case 10:
			tempLandmark = new Landmark("JUST VISITING / IN JAIL", Color.GRAY);
			break;
		case 11:
			tempLandmark = new ColorGroup("ST. CHARLES PLACE", Color.PINK, 140, 100, new int[] {10, 50, 150, 450, 625, 750});
			break;
		case 12:
			tempLandmark = new Utility("ELECTRIC COMPANY", Color.GRAY);
			break;
		case 13:
			tempLandmark = new ColorGroup("STATES AVENUE", Color.PINK, 140, 100, new int[] {10, 50, 150, 450, 625, 750});
			break;
		case 14:
			tempLandmark = new ColorGroup("VIRGINIA AVENUE", Color.PINK, 160, 100, new int[] {12, 60, 180, 500, 700, 900});
			break;
		case 15:
			tempLandmark = new Railroad("PENNSYLVANIA RAILROAD", Color.GRAY);
			break;
		case 16:
			tempLandmark = new ColorGroup("ST. JAMES PLACE", Color.ORANGE, 180, 100, new int[] {14, 70, 200, 550, 750, 950});
			break;
		case 17:
			tempLandmark = new Landmark("COMMUNITY CHEST", Color.GRAY);
			break;
		case 18:
			tempLandmark = new ColorGroup("TENNESSEE AVENUE", Color.ORANGE, 180, 100, new int[] {14, 70, 200, 550, 750, 950});
			break;
		case 19:
			tempLandmark = new ColorGroup("NEW YORK AVENUE", Color.ORANGE, 200, 100, new int[] {16, 80, 220, 600, 800, 1000});
			break;
		case 20:
			tempLandmark = new Landmark("FREE PARKING", Color.GRAY);
			break;
		case 21:
			tempLandmark = new ColorGroup("KENTUCKY AVENUE", Color.RED, 220, 150, new int[] {18, 90, 250, 700, 875, 1050});
			break;
		case 22:
			tempLandmark = new Landmark("CHANCE", Color.GRAY);
			break;
		case 23:
			tempLandmark = new ColorGroup("INDIANA AVENUE", Color.RED, 220, 150, new int[] {18, 90, 250, 700, 875, 1050});
			break;
		case 24:
			tempLandmark = new ColorGroup("ILLINOIS AVENUE", Color.RED, 240, 150, new int[] {20, 100, 300, 750, 925, 1100});
			break;
		case 25:
			tempLandmark = new Railroad("B. & O. RAILROAD", Color.GRAY);
			break;
		case 26:
			tempLandmark = new ColorGroup("ATLANTIC AVENUE", Color.YELLOW, 260, 150, new int[] {22, 110, 330, 800, 975, 1150});
			break;
		case 27:
			tempLandmark = new ColorGroup("VENINOR AVENUE", Color.YELLOW, 260, 150, new int[] {22, 110, 330, 800, 975, 1150});
			break;
		case 28:
			tempLandmark = new Utility("WATER WORKS", Color.GRAY);
			break;
		case 29:
			tempLandmark = new ColorGroup("MARVIN GARDENS", Color.YELLOW, 280, 150, new int[] {24, 120, 360, 850, 1025, 1200});
			break;
		case 30:
			tempLandmark = new Landmark("GO TO JAIL", Color.GRAY);
			break;
		case 31:
			tempLandmark = new ColorGroup("PACIFIC AVENUE", Color.GREEN, 300, 200, new int[] {26, 130, 390, 900, 1100, 1275});
			break;
		case 32:
			tempLandmark = new ColorGroup("NORTH CAROLINA AVENUE", Color.GREEN, 300, 200, new int[] {26, 130, 390, 900, 1100, 1275});
			break;
		case 33:
			tempLandmark = new Landmark("COMMUNITY CHEST", Color.GRAY);
			break;
		case 34:
			tempLandmark = new ColorGroup("PENNSYLVANIA AVENUE", Color.GREEN, 320, 200, new int[] {28, 150, 450, 1000, 1200, 1400});
			break;
		case 35:
			tempLandmark = new Railroad("SHORT LINE", Color.GRAY);
			break;
		case 36:
			tempLandmark = new Landmark("CHANCE", Color.GRAY);
			break;
		case 37:
			tempLandmark = new ColorGroup("PARK PLACE", Color.DARKBLUE, 350, 200, new int[] {35, 175, 500, 1100, 1300, 1500});
			break;
		case 38:
			tempLandmark = new TaxSpace("LUXURY TAX", Color.GRAY, 100);
			break;
		case 39:
			tempLandmark = new ColorGroup("BOARDWALK", Color.DARKBLUE, 400, 200, new int[] {50, 200, 600, 1400, 170, 2000});
			break;
		default:
			break;
		}
		return tempLandmark;
	}
	
	
	/*******************************
	 * Initialize chance or community chest card deck 
	 * Each deck with different card IDs between 1 and 16 
	 * Shuffle the deck 
	 */
	public ArrayList<Integer> cardIDInitialization() {
		ArrayList<Integer> tempArrayList = new ArrayList<>();
		for(int i = 1; i <= 16; i++) {
			tempArrayList.add(i);
		}
		Collections.shuffle(tempArrayList); 
		return tempArrayList;
	}
	
	
	/*******************************
	 * Chance the position to the board grid matrix cell position 
	 */
	public int[] indexToSubscript(int anIndex) {
		if(anIndex >= 0 && anIndex <= 9) {
			return new int[] {10 - anIndex, 10};
		}
		else if(anIndex >= 10 && anIndex <= 19) {
			return new int[] {0, 20 - anIndex};
		}
		else if(anIndex >= 20 && anIndex <= 29) {
			return new int[] {anIndex - 20, 0};
		}
		else {
			return new int[] {10, anIndex - 30};
		}
	}
	
	
	/*******************************
	 * In case that multiple players' tokens land on the same position, 
	 * Assign a spot of 3*3(holding the maximum of 8 tokens) based on the player's id 
	 */
	public void setOrientation(Player aPlayer, int aID) {
		switch(aID) {
		case 0: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.LEFT);
			GridPane.setValignment(aPlayer.getToken(), VPos.TOP);
			break;
		case 1: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.LEFT);
			GridPane.setValignment(aPlayer.getToken(), VPos.CENTER);
			break;
		case 2: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.LEFT);
			GridPane.setValignment(aPlayer.getToken(), VPos.BOTTOM);
			break;
		case 3: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.CENTER);
			GridPane.setValignment(aPlayer.getToken(), VPos.TOP);
			break;
		case 4: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.CENTER);
			GridPane.setValignment(aPlayer.getToken(), VPos.BOTTOM);
			break;
		case 5: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.RIGHT);
			GridPane.setValignment(aPlayer.getToken(), VPos.TOP);
			break;
		case 6: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.RIGHT);
			GridPane.setValignment(aPlayer.getToken(), VPos.CENTER);
			break;
		case 7: 
			GridPane.setHalignment(aPlayer.getToken(), HPos.RIGHT);
			GridPane.setValignment(aPlayer.getToken(), VPos.BOTTOM);
			break;
		default: 
			break;
		}
	}
	
	
	/*******************************
	 * Array to ArrayList 
	 */
	public ArrayList<Integer> getArrayList(int[] anArray) {
		ArrayList<Integer> tempArrayList = new ArrayList<>();
		for(int anInt: anArray) {
			tempArrayList.add(anInt);
		}
		return tempArrayList;
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
