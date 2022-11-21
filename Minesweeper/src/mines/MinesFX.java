package mines;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MinesFX extends Application {
	private Mines m;
	private Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// creating the stage and connecting the FXML file
		this.stage = stage;
		HBox h;
		MyController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MinesFX.fxml"));
			h = loader.load();
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(CreateGrid(h, controller));
		stage.setScene(scene);
		stage.setTitle("The Amazing Minesweeper");
		stage.show();
	}

	public HBox CreateGrid(HBox h, MyController controller) {
		// creating the board grid
		int width = Integer.parseInt(controller.getWidthText().getText());
		int height = Integer.parseInt(controller.getHeightText().getText());
		int mines = Integer.parseInt(controller.getMinesText().getText());
		m = new Mines(height, width, mines);
		GridPane gp = new GridPane();

		// setting the buttons in the greed
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				Button b = new Button(".");
				b.setFont(Font.font("Aharoni", 25));
				b.setPrefWidth(60);
				b.setPrefHeight(60);
				b.setOnMouseClicked(new gameButton(gp, height, width, j, i));
				gp.add(b, j, i);
			}
		
		gp.setPadding(new Insets(10));
		Button b = controller.getResetButton();
		b.setOnAction(new resetGame(h, controller));
		h.getChildren().add(gp);
		h.autosize();
		stage.sizeToScene();
		return h;
	}

	// class for the reset button
	class resetGame implements EventHandler<ActionEvent> {
		private HBox h;
		private MyController controller;

		public resetGame(HBox h, MyController controller) {
			this.h = h;
			this.controller = controller;
		}

		@Override
		public void handle(ActionEvent event) {
			h.getChildren().remove(1);
			CreateGrid(h, controller);
		}
	}

	// class to open a button in the grid
	class gameButton implements EventHandler<MouseEvent> {
		private GridPane gp;
		private int height, width, row, column;

		public gameButton(GridPane gp, int height, int width, int row, int column) {
			this.gp = gp;
			this.height = height;
			this.width = width;
			this.row = row;
			this.column = column;
		}

		@Override
		public void handle(MouseEvent event) {
			MouseButton button = event.getButton();
			boolean notMine = true;

			if (button == MouseButton.PRIMARY)
				//opening this button
				if(!m.get(row, column).equals("F"))
					notMine = m.open(row, column);

			if (button == MouseButton.SECONDARY)
				//setting a flag on this button
				m.toggleFlag(row, column);

			if (!notMine) {
				// if a mine has been clicked
				m.setShowAll(true);
				for (int x = 0; x < height; x++)
					for (int y = 0; y < width; y++)
						if (m.get(y, x).equals("X"))
							((Button) gp.getChildren().get(x * width + y)).setText("X");
				Alert a = new Alert(AlertType.CONFIRMATION);
				a.show();
			}

			else {
			//if it is not a mine
				for (int x = 0; x < height; x++)
					for (int y = 0; y < width; y++) {
						((Button) gp.getChildren().get(x * width + y)).setText(m.get(y, x));
					}
				if (m.isDone()) {
					Alert a = new Alert(AlertType.CONFIRMATION);
					a.setContentText("OMG, you just won!!!");
					a.show();
				}
			}
		}
	}
}
