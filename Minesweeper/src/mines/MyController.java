package mines;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MyController {

	@FXML
	private Button resetButton;

    @FXML
    private TextField heightText;
   
    @FXML
    private TextField minesText;
   
    @FXML
    private TextField widthText;

    public Button getResetButton() {
    	return resetButton;
    }
    
	public TextField getHeightText() {
		return heightText;
	}

	public TextField getMinesText() {
		return minesText;
	}

	public TextField getWidthText() {
		return widthText;
	}
    
    
}
