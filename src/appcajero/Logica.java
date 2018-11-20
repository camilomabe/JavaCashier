package appcajero;

import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Logica {

    private int faseActual;
    private Cajero cajero;
    private Cuenta[] cuentas;
    private Cuenta CuentaActual;
    private String CadenaTeclado = "";
    private String MascaraClave = "";
    private final String Saltos = "<br><br><br><br><br><br><br><br>";
    private int Opcion;
    private boolean TecladoActivo = false;

    public Logica(Cajero cajero) {
        this.cajero = cajero;
        Inicio();
        CargarCuentas();
    }

    private void Inicio() {
        this.cajero.DisplayBajo.setText("<html><body><div Style='color:aqua;'><h1>Bienvenido</h1></div>"
                + "<div Style='color:aqua;'>Oprima un boton para continuar</div></body><html>");
        this.cajero.DisplayDer.setText("");
        this.cajero.DisplayIzq.setText("");
        faseActual = 1;
        CadenaTeclado = "";
    }

    private void CargarCuentas() {
        cuentas = new Cuenta[3];
        cuentas[0] = new Cuenta("Pedro Perez", "1234", 1000000);
        cuentas[1] = new Cuenta("Ana Quintana", "4567", 150000);
        cuentas[2] = new Cuenta("Carlos Lopez", "8901", 800000);
    }

    public void EventoTeclado(String Digito) {
        if (TecladoActivo) {
            if (faseActual == 3) {
                if (!Digito.equals("C") && !Digito.equals("A")) {
                    CadenaTeclado += Digito;
                    MascaraClave += "* ";
                    this.cajero.DisplayBajo.setText("<html><body style='color:white'>"
                            + MascaraClave + "</body></html>");
                } else if (Digito.equals("A")) {
                    FaseTres();
                } else {
                    Inicio();
                }
            } else if (faseActual == 6) {
                if (!Digito.equals("C") && !Digito.equals("A")) {
                    CadenaTeclado += Digito;
                    int entero = Integer.parseInt(CadenaTeclado);
                    NumberFormat formato = NumberFormat.getCurrencyInstance();
                    String Saldo = formato.format(entero);
                    this.cajero.DisplayBajo.setText("<html><body style='color:green'><h1>"
                            + Saldo + "</h1> </body></html>");
                } else if (Digito.equals("A")) {
                    int entero = Integer.parseInt(CadenaTeclado);
                    FaseCinco(entero);
                } else {
                    Inicio();
                }
            }
        }
    }

    private void FaseUno() {
        this.cajero.DisplayDer.setText("<html><body>Retiro" + Saltos + "</body></html>");
        this.cajero.DisplayIzq.setText("<html><body>Solicitar saldo" + Saltos + "</body></html>");
        this.cajero.DisplayBajo.setText("<html><body'><h1 style='color:yellow;'>Bienvenido</h1></body></html>");
        faseActual = 2;
    }

    private void FaseDos(int Opc) {
        this.cajero.DisplayDer.setText("");
        this.cajero.DisplayIzq.setText("");
        this.cajero.DisplayBajo.setText("<html><body'><h1 style='color:yellow;'>Digite su contraseña</h1></body></html>");
        faseActual = 3;
        Opcion = Opc;
        CadenaTeclado = "";
        MascaraClave = "";
        TecladoActivo = true;

    }

    private void FaseTres() {
        faseActual = 4;
        TecladoActivo = false;
        for (Cuenta cuenta : cuentas) {
            if (cuenta.Clave.equals(CadenaTeclado)) {
                CuentaActual = cuenta;
                this.cajero.DisplayBajo.setText("<html><body'><h1 style='color:yellow;'>Bienvenido señor(a) " + cuenta.Nombre + " </h1></body></html>");
                Timer timer = new Timer(1000, (ActionEvent e) -> {
                    FaseCuatro();
                });
                timer.setRepeats(false);
                timer.start();
                return;
            }
        }
        this.cajero.DisplayBajo.setText("<html><body'><style='color:red;'>Clave errada</h1></body></html>");
        RegresoInicio();
    }

    private void FaseCuatro() {
        if (Opcion == 1) {
            MostrarSaldo(0);
        } else {
            String Texto = "$ 500.000<br><br>"
                    + "$ 300.000<br><br>"
                    + "$ 100.000<br><br>"
                    + "$ Otro valor<br><br>";
            this.cajero.DisplayIzq.setText("<html><body>" + Texto + "</body></html>");
            Texto = "$ 600.000<br><br>"
                    + "$ 400.000<br><br>"
                    + "$ 200.000<br><br>"
                    + "$ 50.000<br><br>";
            this.cajero.DisplayDer.setText("<html><body>" + Texto + "</body></html>");

        }
        faseActual = 5;

    }

    void RegresoInicio() {
        Timer timer = new Timer(3000, (ActionEvent e) -> {
            Inicio();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void FaseCinco(int opc) {

        if (Opcion == 1 && opc == 1) {
            Inicio();
        } else if (Opcion == 1) {
            FaseSiete();
        } else {

            /*validar valor multiplos de 10.000*/
            /*validar saldo*/
            /*mostrar saldo con las denominaciones de los billetes*/
            Opcion = opc;
            Opcion = Validarsaldo(Opcion);
            if (Opcion > CuentaActual.Saldo) {
                SaldoInsuficiente();
            } else {
                if (Opcion == 0) {
                    this.cajero.DisplayBajo.setText("<html><body'><style='color:red;'>"
                            + "Los valores deben ser superiores a 10.000"
                            + "</h1></body></html>");
                    RegresoInicio();
                } else if (Opcion == 1) {
                    this.cajero.DisplayBajo.setText("<html><body'><style='color:red;'>"
                            + "Los valores deben ser decenas de mil"
                            + "</h1></body></html>");
                    RegresoInicio();
                } else {
                    CuentaActual.Saldo -= Opcion;
                    MostrarSaldo(1);
                }
            }

            faseActual = 7;
        }

    }

    void SaldoInsuficiente() {
        this.cajero.DisplayBajo.setText("<html><body'><style='color:red;'>"
                + "Saldo insuficiente"
                + "</h1></body></html>");
        RegresoInicio();
    }

    int Validarsaldo(int valor) {
        int valor2 = valor / 10000;

        if (valor2 < 1) {
            valor = 0;
        } else if (valor % 10000 != 0) {
            valor = 1;
        } else {
            valor = valor2 * 10000;
        }
        return valor;
    }

    public void FaseSiete() {
        java.util.Date fecha = new Date();
        NumberFormat formato = NumberFormat.getCurrencyInstance();
        String texto = "Banco RobaMas\n"
                + "Cliente: " + CuentaActual.Nombre + "\n"
                + "Fecha: " + fecha + "\n"
                + "Saldo: " + formato.format(CuentaActual.Saldo);
        JOptionPane.showOptionDialog(this.cajero, texto, "",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{" OK "}, "OK");
        Inicio();
    }

    public String Valorbilletes(int valor) {
        int b = 50000, c = 20000, d = 10000, a = valor;
        int x = 0, y = 0, z = 0;
        for (int i = 0; a != 0; i++) {
            if (a > b) {
                a -= b;
                x++;
            } else if (a > c) {
                a -= c;
                y++;
            } else {
                a -= d;
                z++;
            }
        }
        String vale = String.valueOf("Valor       Cantidad<br>" + b + "------" + x + "<br>" + c + "------" + y + "<br>" + d + "------" + z + "<br><br>");
        FaseSiete();
        RegresoInicio();
        return vale;
    }

    private void MostrarSaldo(int don) {
        NumberFormat formato = NumberFormat.getCurrencyInstance();
        String saldo = formato.format(CuentaActual.Saldo);
        if (don == 1) {
            this.cajero.DisplayDer.setText("<html><body>" + Valorbilletes(Opcion) + "" + Saltos + "</body></html>");
            this.cajero.DisplayIzq.setText("<html><body>La denominacion<br>de los billetes son" + Saltos + "</body></html>");
        }
        if (don == 0) {
            this.cajero.DisplayDer.setText("<html><body>Desea imprimir su recibo " + Saltos + "</body></html>");
            this.cajero.DisplayIzq.setText("<html><body> Salir" + Saltos + "</body></html>");
        }
        this.cajero.DisplayBajo.setText("<html><body style='color yellow;'>Su saldo a la fecha es de: "
                + "<h1 style='color green;'>" + saldo + "</h1>"
                + "</body></html>");

    }

    public void EventoBoton1() {
        switch (faseActual) {
            case 1:
                FaseUno();
                break;
            case 2:
                FaseDos(1);
                break;
            case 5:
                if (Opcion == 1) {
                    FaseCinco(1);
                } else {
                    FaseCinco(500000);
                }
                break;
            case 7:
                Inicio();
                break;
            default:
                break;
        }

    }

    public void EventoBoton2() {
        if (faseActual == 5) {
            FaseCinco(300000);
        }
    }

    public void EventoBoton3() {
        if (faseActual == 5) {
            FaseCinco(100000);
        }
    }

    public void EventoBoton4() {
        if (faseActual == 5) {
            TecladoActivo = true;
            CadenaTeclado = "";
            this.cajero.DisplayDer.setText("  ");
            this.cajero.DisplayIzq.setText("  ");
            faseActual = 6;
        }
    }

    public void EventoBoton5() {
        switch (faseActual) {
            case 1:
                FaseUno();
                break;
            case 2:
                FaseDos(2);
                break;
            case 5:
                if (Opcion == 1) {
                    FaseCinco(2);
                } else {
                    FaseCinco(600000);
                }
                break;
            case 7:
                Inicio();
                break;
            default:
                break;
        }

    }

    public void EventoBoton6() {
        if (faseActual == 5) {
            FaseCinco(400000);
        }
    }

    public void EventoBoton7() {
        if (faseActual == 5) {
            FaseCinco(200000);
        }
    }

    public void EventoBoton8() {
        if (faseActual == 5) {
            FaseCinco(50000);
        }
    }

}
