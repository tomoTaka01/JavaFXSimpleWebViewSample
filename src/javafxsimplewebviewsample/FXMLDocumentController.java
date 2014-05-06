package javafxsimplewebviewsample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Controller for loading URL
 * 
 * @author tomo
 */
public class FXMLDocumentController implements Initializable {
    private WebEngine engine;
    
    private LoadingState initState;
    private LoadingState startLoadState;
    private LoadingState startCancelState;
    private LoadingState loadCompletedState;
    private LoadingState loadFailedState;
    private LoadingState loadCanceledState;
    
    @FXML TextField urlText;
    @FXML WebView webView;
    @FXML Label loadState;
    @FXML ProgressIndicator indicator;
    @FXML Button loadButton;
    @FXML Button cancelButton;
    
    /**
     * Action for the load button
     * 
     * @param event 
     */
    @FXML private void loadURL(Event event){
        startLoadState.apply();
        String url = urlText.getText();
        this.engine.load(url);
    }
    /**
     * Action for the canel load button
     * 
     * @param event 
     */
    @FXML private void cancelLoad(Event event) {
        startCancelState.apply();
        this.engine.getLoadWorker().cancel();
    }
    /**
     * initialize for the fxml
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setLoadingState();
        initState.apply();
        this.engine = webView.getEngine();
        // binding the progressIndicator to the Web LoadWorker
        this.indicator.progressProperty().bind(this.engine.getLoadWorker().progressProperty());
        this.engine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    switch (newState){ 
                        case SUCCEEDED:
                            loadCompletedState.apply();
                            break;
                        case CANCELLED:
                            loadCanceledState.apply();
                            break;
                        case FAILED:
                            loadFailedState.apply();
                            break;
                    };
                });
    }    

    /**
     * set the loading states
     */
    private void setLoadingState() {
        initState = () -> {
            urlText.setDisable(false);
            loadButton.setDisable(false);
            cancelButton.setDisable(true);
            loadState.setText("");            
            webView.setVisible(false);
            indicator.setVisible(false);
        };
        startLoadState = () -> {
            urlText.setDisable(true);
            loadButton.setDisable(true);
            cancelButton.setDisable(false);
            loadState.setTextFill(Color.BLACK);
            loadState.setText("loading...");            
            webView.setVisible(false);
            indicator.setVisible(true);
        };
        startCancelState = () -> {
            cancelButton.setDisable(true);
            loadState.setTextFill(Color.BLACK);
            loadState.setText("cancelling....");            
        };
        loadCompletedState = () -> {
            urlText.setDisable(false);
            loadButton.setDisable(false);
            cancelButton.setDisable(true);
            loadState.setTextFill(Color.BLACK);
            loadState.setText("completed!");            
            webView.setVisible(true);
            indicator.setVisible(false);
        };
        loadFailedState = () -> {
            urlText.setDisable(false);
            loadButton.setDisable(false);
            cancelButton.setDisable(true);
            loadState.setTextFill(Color.RED);
            loadState.setText("failed!!!");            
            webView.setVisible(false);
            indicator.setVisible(false);
        };
        loadCanceledState = () -> {
            urlText.setDisable(false);
            loadButton.setDisable(false);
            cancelButton.setDisable(true);
            loadState.setTextFill(Color.ORANGE);
            loadState.setText("canceled!");            
            webView.setVisible(false);
            indicator.setVisible(false);
        };
    }
    
}

/**
 * interface for the loading State
 * 
 * @author tomo
 */
@FunctionalInterface
interface LoadingState{
    void apply();
}
