package com.agungadiariawan.portscanner;

import com.agungadiariawan.portscanner.helpers.ValidasiForm;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.util.Duration;


public class MainControllers implements Initializable {

    @FXML private FontAwesomeIconView iconAntara;
    @FXML private FontAwesomeIconView iconDefault;

    @FXML public Label notifLabel;
    @FXML public FontAwesomeIconView notifIcon;

    @FXML private Button btnCheckPortSingel;
    @FXML private Button btnCheckPortAntara;
    @FXML private Button btnClearForm;
    @FXML private Button btnCheckPortDefault;

    @FXML private TextField formIpInput;
    @FXML private TextField formPortInput;
    @FXML private TextField formStartPort;
    @FXML private TextField formEndPort;

    @FXML private TableView<PortResult> tableviewcheckport;
    @FXML private TableColumn<PortResult, String> tblipcek;
    @FXML private TableColumn<PortResult, String> tblport;
    @FXML private TableColumn<PortResult, String> tblstatus;

    private final ObservableList<PortResult> portData = FXCollections.observableArrayList();
    private RotateTransition spinnerAnimation;
    private FontAwesomeIconView currentSpinningIcon = null;



    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        tblipcek.setCellValueFactory(cell -> cell.getValue().ipProperty());
        tblport.setCellValueFactory(cell -> cell.getValue().portProperty());
        tblstatus.setCellValueFactory(cell -> cell.getValue().statusProperty());

        tableviewcheckport.setItems(portData);
    }



    @FXML
    private void handelCheckPortDefault() {
        new Thread(() -> {
            setScanningState(true, btnCheckPortDefault, "Cek Port Default", iconDefault);
            try {
                String ip = ValidasiForm.sanitizeIpInput(formIpInput.getText().trim());
                if (ip.isEmpty()) {
                    allert("❌ IP User tidak Valid Periksa Kembali.");
                    return;
                }

                Platform.runLater(() -> portData.clear());
                List<Integer> ports = PortDefault.getDefaultPorts();

                for (int port : ports) {
                    String status;
                    try (Socket socket = new Socket()) {
                        socket.connect(new java.net.InetSocketAddress(ip, port), 300);
                        status = "✅ Terbuka";
                    } catch (Exception e) {
                        status = "❌ Tertutup";
                    }

                    String finalStatus = status;
                    Platform.runLater(() -> {
                        portData.add(new PortResult(ip, String.valueOf(port), finalStatus));
                        tableviewcheckport.scrollTo(portData.size() - 1);
                    });

                    Thread.sleep(50);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                setScanningState(false, btnCheckPortDefault, "Cek Port Default", iconDefault);
            }
        }).start();
    }

    @FXML
    private void handelCheckSingel() {
        new Thread(() -> {
            try {
                String ip = ValidasiForm.sanitizeIpInput(formIpInput.getText().trim());
                Integer port = ValidasiForm.sanitizePortInput(formPortInput.getText().trim());

                if (ip.isEmpty()) {
                    allert("❌ IP User tidak Valid Periksa Kembali.");
                    return;
                }

                if (port == null) {
                    allert("❌ Port tidak boleh kosong.");
                    return;
                }


                if (port < 1 || port > 65535) {
                    allert("❌ Port harus dalam rentang 1–65535.");
                    return;
                }

                Platform.runLater(() -> portData.clear());

                String status;
                try (Socket socket = new Socket()) {
                    socket.connect(new java.net.InetSocketAddress(ip, port), 300);
                    status = "✅ Terbuka";
                } catch (Exception e) {
                    status = "❌ Tertutup";
                }

                String finalStatus = status;
                Platform.runLater(() -> {
                    portData.add(new PortResult(ip, String.valueOf(port), finalStatus));
                    tableviewcheckport.scrollTo(portData.size() - 1);
                });

            } catch (NumberFormatException e) {
                allert("❌ Port harus berupa angka.");
            } catch (Exception e) {
                e.printStackTrace();
                allert("⚠️ Terjadi kesalahan: " + e.getMessage());
            }
        }).start();
    }

    @FXML
    private void handelClearFormPort() {
        formIpInput.clear();
        formPortInput.clear();
        formStartPort.clear();
        formEndPort.clear();
        portData.clear();
    }

    @FXML
    private void handelCheckPortAntara() {
        new Thread(() -> {
            setScanningState(true, btnCheckPortAntara, "Cek Antara Port", iconAntara);
            try {
                String ip = ValidasiForm.sanitizeIpInput(formIpInput.getText().trim());

                Integer startPort = ValidasiForm.sanitizePortInput(formStartPort.getText().trim());
                Integer endPort = ValidasiForm.sanitizePortInput(formEndPort.getText().trim());

                if (ip.isEmpty()) {
                    allert("❌ IP User tidak Valid Periksa Kembali.");
                    return;
                }

                if (startPort == null) {
                    allert("❌ Port Start tidak boleh kosong.");
                    return;
                }

                if (endPort == null) {
                    allert("❌ Port End tidak boleh kosong.");
                    return;
                }


                if (startPort < 1 || endPort > 65535 || startPort > endPort) {
                    allert("❌ Range port tidak valid (harus 1–65535 dan start ≤ end).");
                    return;
                }

                Platform.runLater(() -> portData.clear());

                for (int port = startPort; port <= endPort; port++) {
                    String status;
                    try (Socket socket = new Socket()) {
                        socket.connect(new java.net.InetSocketAddress(ip, port), 300);
                        status = "✅ Terbuka";
                    } catch (Exception e) {
                        status = "❌ Tertutup";
                    }

                    String finalStatus = status;
                    int finalPort = port;
                    Platform.runLater(() -> {
                        portData.add(new PortResult(ip, String.valueOf(finalPort), finalStatus));
                        tableviewcheckport.scrollTo(portData.size() - 1);
                    });

                    Thread.sleep(30);
                }
            } catch (NumberFormatException nfe) {
                allert("❌ Start dan End port harus berupa angka.");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                setScanningState(false, btnCheckPortAntara, "Cek Antara Port", iconAntara);
            }
        }).start();
    }

    private void allert(String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Informasi");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    private void handelBtnClose(ActionEvent event) {
        Platform.runLater(() -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void handelMinimize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }



    private boolean isMaximized = false;
    private double previousWidth;
    private double previousHeight;
    @FXML
    private void handelMaximize(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        if (isMaximized) {
            stage.setWidth(previousWidth);
            stage.setHeight(previousHeight);
            stage.centerOnScreen();
            isMaximized = false;
        } else {
            previousWidth = stage.getWidth();
            previousHeight = stage.getHeight();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            isMaximized = true;
        }
    }

    private boolean isMaximizedHeader = false;
    private double previousWidthHeader;
    private double previousHeightHeader;
    private void handelMaximizeHeader(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            if (isMaximizedHeader) {
                stage.setWidth(previousWidthHeader);
                stage.setHeight(previousHeightHeader);
                stage.centerOnScreen();
                isMaximizedHeader = false;
            } else {
                previousWidthHeader = stage.getWidth();
                previousHeightHeader = stage.getHeight();
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
                isMaximizedHeader = true;
            }
        }
    }

    private void setScanningState(boolean isScanning, Button activeButton, String defaultText, FontAwesomeIconView icon) {
        Platform.runLater(() -> {
            boolean lock = isScanning;

            btnCheckPortAntara.setDisable(lock);
            btnCheckPortSingel.setDisable(lock);
            btnCheckPortDefault.setDisable(lock);
            btnClearForm.setDisable(lock);

            if (isScanning) {
                icon.setGlyphName("SPINNER");
                startSpinnerAnimation(icon);
                activeButton.setText("Scanning...");
            } else {
                stopSpinnerAnimation();

                if (activeButton == btnCheckPortAntara) icon.setGlyphName("SEARCH");
                if (activeButton == btnCheckPortSingel) icon.setGlyphName("SEARCH");
                if (activeButton == btnCheckPortDefault) icon.setGlyphName("SEARCH");
                if (activeButton == btnClearForm) icon.setGlyphName("TRASH");

                activeButton.setText(defaultText);
            }
        });
    }

    private void startSpinnerAnimation(FontAwesomeIconView icon) {
        if (spinnerAnimation != null) spinnerAnimation.stop();

        spinnerAnimation = new RotateTransition(Duration.millis(800), icon);
        spinnerAnimation.setByAngle(360);
        spinnerAnimation.setCycleCount(RotateTransition.INDEFINITE);
        spinnerAnimation.setInterpolator(javafx.animation.Interpolator.LINEAR);
        spinnerAnimation.play();
        currentSpinningIcon = icon;

    }

    private void stopSpinnerAnimation() {
        if (spinnerAnimation != null) {
            spinnerAnimation.stop();
            spinnerAnimation = null;
        }
        if (currentSpinningIcon != null) {
            currentSpinningIcon.setRotate(0);
            currentSpinningIcon = null;
        }
    }
}
