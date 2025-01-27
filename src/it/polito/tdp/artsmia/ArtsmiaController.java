/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {

	private Model model;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		this.model.creaGrafo();
		txtResult.setText("Grafo creato!\n");
    	txtResult.appendText("# Vertici : " + this.model.getNumVertici() + "\n");
    	txtResult.appendText("# Archi : " + this.model.getNumArchi() + "\n");
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {
		
		try {
			int objectId = Integer.parseInt(txtObjectId.getText());
			
			ArtObject o = this.model.getArtObject(objectId);
			
			if (o == null) {
				txtResult.setText("Nessun oggetto associato a tale identificativo!\n");
				return;
			}
			
			int dimensione = this.model.getSizeComponenteConnessa(o);
			txtResult.setText("La componente connessa all'oggetto " + o.getId() + " ha una dimensione pari a " + dimensione);
			
			for (int i = 2; i <= dimensione; i++) {
				boxLUN.getItems().add(i);
			}
			
			
		} catch (NumberFormatException e) {
			txtResult.setText("Per favore inserire un identificativo valido!\n");
			return;
		}
	
	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		
		Integer lunghezza = boxLUN.getValue();
		if (lunghezza == null) {
			txtResult.setText("Per favore selezionare la lunghezza dalla tendina!\n");
			return;
		}
		
		try {
			int objectId = Integer.parseInt(txtObjectId.getText());
			
			ArtObject o = this.model.getArtObject(objectId);
			
			if (o == null) {
				txtResult.setText("Nessun oggetto associato a tale identificativo!\n");
				return;
			}
			
			List<ArtObject> cammino = this.model.calcolaCammino(lunghezza, o);
			Collections.sort(cammino);
			txtResult.appendText("Il cammino di peso '" + this.model.calcolaPesoCammino(cammino) + "' è: \n");
			for (ArtObject obj : cammino) {
				txtResult.appendText(obj + "\n");
			}
			
		} catch (NumberFormatException e) {
			txtResult.setText("Per favore inserire un identificativo valido!\n");
			return;
		}
	
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}
	
	public void setModel(Model model) {
    	this.model = model;
    }
	
}
