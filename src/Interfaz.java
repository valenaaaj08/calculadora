
import java.awt.EventQueue;
import javax.swing.JTextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class Interfaz extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea pantalla;
	private JButton boton2;
	private JButton boton3;
	private JButton boton4;
	private JButton boton5;
	private JButton boton6;
	private JButton boton7;
	private JButton boton8;
	private JButton boton9;
	private JButton boton0;
	private JLabel label_tm;
	private JLabel lblCalc;
	private JButton botonC;
	private JButton botonIgual;
	private JButton botonAns;
	private JButton botonAc;
	private JButton botonPunto;
	private JButton botonSuma;
	private JLabel label_operaciones_basicas;
	private JButton botonVectores;
	private JLabel lblOperacionesVectores;
	private JButton botonMatrices;
	private JButton botonEcuaciones;
	private JLabel lblOperacionesMatrices;

int estadoOperacion = 0;  
float[] coef2x2 = new float[6];  
int contadorEcuacion2x2 = 0;
float[] coef3x3 = new float[12]; 
int contadorEcuacion3x3 = 0;
int cantVectores = 0;
int cantElementos = 0;
int operacionVector;
int vectorActual = 0;
int elementoActual = 0;
float[][] vectores;
String bufferNumero = "";
String ultimoResultado = "";
int opcionMatriz = 0;
int filas = 0, columnas = 0;
float[][][] matrices;
int matrizActual = 0, filaActual = 0, columnaActual = 0;
float escalar;
int filasA, columnasA, filasB;
float[][] matrizA;
float[][] matrizB;




	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Interfaz frame = new Interfaz();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	private float determinante(float[][] m) {
	    int n = m.length;
	    if (n == 1) return m[0][0];
	    if (n == 2) return m[0][0]*m[1][1] - m[0][1]*m[1][0];
	    float det = 0;
	    for (int j = 0; j < n; j++) {
	        float[][] minor = new float[n-1][n-1];
	        for (int i = 1; i < n; i++)
	            for (int k = 0; k < n; k++)
	                if (k != j)
	                    minor[i-1][k < j ? k : k-1] = m[i][k];
	        det += Math.pow(-1, j) * m[0][j] * determinante(minor);
	    }
	    return det;
	}
	


	private float[][] convertirA2D(float[][][] array3D, int posicion, int filas, int columnas) {
	    float[][] resultado = new float[filas][columnas];
	    for (int i = 0; i < filas; i++)
	        for (int j = 0; j < columnas; j++)
	            resultado[i][j] = array3D[posicion][i * columnas + j][0];
	    return resultado;
	}

	private float[][] transpuesta(float[][] matriz) {
	    int filas = matriz.length;
	    int columnas = matriz[0].length;
	    float[][] resultado = new float[columnas][filas];
	    for (int i = 0; i < filas; i++)
	        for (int j = 0; j < columnas; j++)
	            resultado[j][i] = matriz[i][j];
	    return resultado;
	}

	private float[][] inversa(float[][] A) {
	    int n = A.length;
	    float[][] inversa = new float[n][n];
	    float[][] extendida = new float[n][2 * n];

	    for (int i = 0; i < n; i++) {
	        for (int j = 0; j < n; j++)
	            extendida[i][j] = A[i][j];
	        extendida[i][n + i] = 1;
	    }

	    for (int i = 0; i < n; i++) {
	        float pivote = extendida[i][i];
	        if (pivote == 0)
	            return null; 
	        for (int j = 0; j < 2 * n; j++)
	            extendida[i][j] /= pivote;

	        for (int k = 0; k < n; k++) {
	            if (k != i) {
	                float factor = extendida[k][i];
	                for (int j = 0; j < 2 * n; j++)
	                    extendida[k][j] -= factor * extendida[i][j];
	            }
	        }
	    }

	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            inversa[i][j] = extendida[i][n + j];

	    return inversa;
	}

	private float[][] multiplicarMatrices(float[][] A, float[][] B) {
	    int filasA = A.length;
	    int columnasA = A[0].length;
	    int columnasB = B[0].length;
	    float[][] resultado = new float[filasA][columnasB];
	    for (int i = 0; i < filasA; i++)
	        for (int j = 0; j < columnasB; j++)
	            for (int k = 0; k < columnasA; k++)
	                resultado[i][j] += A[i][k] * B[k][j];
	    return resultado;
	}

	private void imprimirMatriz(float[][] matriz) {
	    for (float[] fila : matriz) {
	        for (float num : fila)
	            pantalla.append(num + " ");
	        pantalla.append("\n");
	    }
	}


	public Interfaz() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 528, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(183, 189, 172));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton boton1 = new JButton("1");
		boton1.setForeground(Color.WHITE);
		boton1.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton1.setBackground(new Color(109, 7, 26));
		boton1.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton1.setBounds(20, 145, 57, 23);
		boton1.addActionListener(e -> {
			bufferNumero += "1";
			pantalla.append("1");
		});
		contentPane.add(boton1);

		pantalla = new JTextArea();
		pantalla.setLineWrap(true);
		pantalla.setWrapStyleWord(true);
		pantalla.setEditable(false);
		pantalla.setBackground(new Color(198, 224, 180));
		pantalla.setBorder(BorderFactory.createMatteBorder(3, 3, 6, 6, new Color(100, 100, 100)));
		pantalla.setFont(new Font("Monospaced", Font.BOLD, 15));

		JScrollPane scroll = new JScrollPane(pantalla);
		scroll.setBounds(20, 32, 340, 77);
		contentPane.add(scroll);

		boton2 = new JButton("2");
		boton2.setForeground(Color.WHITE);
		boton2.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton2.setBackground(new Color(109, 7, 26));
		boton2.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton2.setBounds(87, 145, 57, 23);
		boton2.addActionListener(e -> {
			bufferNumero += "2";
			pantalla.append("2");
		});
		contentPane.add(boton2);

		boton3 = new JButton("3");
		boton3.setForeground(Color.WHITE);
		boton3.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton3.setBackground(new Color(109, 7, 26));
		boton3.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton3.setBounds(154, 145, 57, 23);
		boton3.addActionListener(e -> {
			bufferNumero += "3";
			pantalla.append("3");
		});
		contentPane.add(boton3);

		boton4 = new JButton("4");
		boton4.setForeground(Color.WHITE);
		boton4.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton4.setBackground(new Color(109, 7, 26));
		boton4.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton4.setBounds(20, 179, 57, 23);
		boton4.addActionListener(e -> {
			bufferNumero += "4";
			pantalla.append("4");
		});
		contentPane.add(boton4);

		boton5 = new JButton("5");
		boton5.setForeground(Color.WHITE);
		boton5.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton5.setBackground(new Color(109, 7, 26));
		boton5.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton5.setBounds(87, 179, 57, 23);
		boton5.addActionListener(e -> {
			bufferNumero += "5";
			pantalla.append("5");
		});
		contentPane.add(boton5);

		boton6 = new JButton("6");
		boton6.setForeground(Color.WHITE);
		boton6.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton6.setBackground(new Color(109, 7, 26));
		boton6.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton6.setBounds(154, 179, 57, 23);
		boton6.addActionListener(e -> {
			bufferNumero += "6";
			pantalla.append("6");
		});
		contentPane.add(boton6);

		boton7 = new JButton("7");
		boton7.setForeground(Color.WHITE);
		boton7.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton7.setBackground(new Color(109, 7, 26));
		boton7.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton7.setBounds(20, 213, 57, 23);
		boton7.addActionListener(e -> {
			bufferNumero += "7";
			pantalla.append("7");
		});
		contentPane.add(boton7);

		boton8 = new JButton("8");
		boton8.setForeground(Color.WHITE);
		boton8.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton8.setBackground(new Color(109, 7, 26));
		boton8.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton8.setBounds(87, 213, 57, 23);
		boton8.addActionListener(e -> {
			bufferNumero += "8";
			pantalla.append("8");
		});
		contentPane.add(boton8);

		boton9 = new JButton("9");
		boton9.setForeground(Color.WHITE);
		boton9.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton9.setBackground(new Color(109, 7, 26));
		boton9.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton9.setBounds(154, 213, 57, 23);
		boton9.addActionListener(e -> {
			bufferNumero += "9";
			pantalla.append("9");
		});
		contentPane.add(boton9);

		boton0 = new JButton("0");
		boton0.setForeground(Color.WHITE);
		boton0.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		boton0.setBackground(new Color(109, 7, 26));
		boton0.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		boton0.setBounds(87, 247, 57, 23);
		boton0.addActionListener(e -> {
			bufferNumero += "0";
			pantalla.append("0");
		});
		contentPane.add(boton0);

		JLabel label_calcboy = new JLabel("CalcBoy");
		label_calcboy.setBounds(51, 112, 46, 23);
		label_calcboy.setFont(new Font("Segoe UI Black", Font.BOLD | Font.ITALIC, 11));
		contentPane.add(label_calcboy);

		JLabel label_empresa = new JLabel("empresa");
		label_empresa.setBounds(20, 120, 46, 14);
		label_empresa.setFont(new Font("Sylfaen", Font.ITALIC, 8));
		contentPane.add(label_empresa);

		label_tm = new JLabel("tm");
		label_tm.setFont(new Font("Sylfaen", Font.ITALIC, 8));
		label_tm.setBounds(96, 120, 46, 14);
		contentPane.add(label_tm);

		lblCalc = new JLabel("CalcBoy FX-90");
		lblCalc.setBounds(434, 297, 78, 14);
		lblCalc.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		contentPane.add(lblCalc);

		botonC = new JButton("C");
		botonC.setForeground(Color.WHITE);
		botonC.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonC.setBackground(new Color(109, 7, 26));
		botonC.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonC.setBounds(232, 213, 57, 23);
		contentPane.add(botonC);
		botonC.addActionListener(e -> {
			if (!bufferNumero.isEmpty()) {
				bufferNumero = bufferNumero.substring(0, bufferNumero.length() - 1);
				String texto = pantalla.getText();
				if (!texto.isEmpty()) {
					pantalla.setText(texto.substring(0, texto.length() - 1));
				}
			}
		});

		botonIgual = new JButton("=");
		botonIgual.setForeground(Color.WHITE);
		botonIgual.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonIgual.setBackground(new Color(109, 7, 26));
		botonIgual.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonIgual.setBounds(154, 247, 57, 23);
		contentPane.add(botonIgual);
		botonIgual.addActionListener(e -> {
		    try {
		        if (estadoOperacion == -1) {  
		            int opcion = Integer.parseInt(bufferNumero.trim());
		            bufferNumero = "";
		            switch (opcion) {
		            case 1:
		            case 2: 
		                operacionVector = opcion; 
		                pantalla.append("\nIngrese cantidad de vectores (minimo 2): ");
		                estadoOperacion = 1;
		                break;
		                case 3: 
		                    pantalla.append("\nIngrese cantidad de elementos del vector: ");
		                    estadoOperacion = 4;
		                    break;
		                case 4: 
		                    pantalla.append("\nIngrese cantidad de elementos por vector: ");
		                    estadoOperacion = 7;
		                    break;
		                case 5: 
		                    pantalla.append("\nIngrese cantidad de elementos (debe ser 3): ");
		                    estadoOperacion = 9;
		                    break;
		                default:
		                    pantalla.append("\nOpcion no implementada");
		                    estadoOperacion = 0;
		                    break;
		            }
		            return;
		        }

		        if (estadoOperacion == -2) {
		            opcionMatriz = Integer.parseInt(bufferNumero.trim());
		            bufferNumero = "";
		            if (opcionMatriz >= 1 && opcionMatriz <= 4) {
		                pantalla.append("\nIngrese cantidad de filas: ");
		                estadoOperacion = 20;
		            } else if (opcionMatriz == 5) {
		                pantalla.append("\nIngrese cantidad de filas: ");
		                estadoOperacion = 110;  
		            } else if (opcionMatriz == 6) {
		                pantalla.append("\nIngrese orden de la matriz cuadrada: ");
		                estadoOperacion = 100;  
		            } else if (opcionMatriz == 7) {
		                pantalla.append("\nIngrese orden de la matriz cuadrada: ");
		                estadoOperacion = 120;  
		            } else if (opcionMatriz == 8) {
		                pantalla.append("\nIngrese orden de la matriz cuadrada: ");
		                estadoOperacion = 130;  
		            } else {
		                pantalla.append("\nOpcion no valida");
		                estadoOperacion = 0;
		            }
		            return;
		        }
		        if (estadoOperacion == -3) { 
		            int opcion = Integer.parseInt(bufferNumero.trim());
		            bufferNumero = "";
		            if (opcion == 1) {
		                pantalla.append("\nIngrese a1: ");
		                estadoOperacion = 300;
		            } else if (opcion == 2) {
		                pantalla.append("\nIngrese a1: ");
		                estadoOperacion = 301;
		            } else {
		                pantalla.append("\nOpcion no valida");
		                estadoOperacion = 0;
		            }
		            return;
		        }
		        

		        if (estadoOperacion == 20) {
		            filas = Integer.parseInt(bufferNumero.trim());
		            bufferNumero = "";
		            pantalla.append("\nIngrese cantidad de columnas: ");
		            estadoOperacion = 21;
		            return;
		        }

		        if (estadoOperacion == 21) {
		            columnas = Integer.parseInt(bufferNumero.trim());
		            bufferNumero = "";

		            if (opcionMatriz == 4) {
		                columnasA = columnas;
		                pantalla.append("\nIngrese cantidad de filas de la segunda matriz: ");
		                estadoOperacion = 241;
		            } else if (opcionMatriz == 3) {
		                pantalla.append("\nIngrese escalar: ");
		                estadoOperacion = 23;
		            } else if (opcionMatriz == 5) {
		                pantalla.append("\nIngrese cantidad de columnas de la segunda matriz: ");
		                estadoOperacion = 24;
		            } else {
		                pantalla.append("\nIngrese elemento [1][1] de la matriz 1: ");
		                matrices = new float[2][filas * columnas][1];
		                matrizActual = 0;
		                filaActual = 0;
		                columnaActual = 0;
		                estadoOperacion = 22;
		            }
		            return;
		        }

		   
		        if (estadoOperacion == 22) {
		            matrices[matrizActual][filaActual * columnas + columnaActual][0] = Float.parseFloat(bufferNumero.trim());
		            bufferNumero = "";
		            columnaActual++;
		            if (columnaActual == columnas) {
		                columnaActual = 0;
		                filaActual++;
		            }
		            if (filaActual == filas) {
		                matrizActual++;
		                filaActual = 0;
		                columnaActual = 0;
		                if (matrizActual == 2) {
		                    float[][] resultado = new float[filas][columnas];
		                    for (int i = 0; i < filas; i++) {
		                        for (int j = 0; j < columnas; j++) {
		                            float val1 = matrices[0][i * columnas + j][0];
		                            float val2 = matrices[1][i * columnas + j][0];
		                            switch (opcionMatriz) {
		                                case 1: resultado[i][j] = val1 + val2; break;
		                                case 2: resultado[i][j] = val1 - val2; break;
		                            }
		                        }
		                    }
		                    pantalla.append("\nResultado:\n");
		                    for (float[] fila : resultado) {
		                        for (float num : fila) pantalla.append(num + " ");
		                        pantalla.append("\n");
		                    }
		                    estadoOperacion = 0;
		                } else {
		                    pantalla.append("\nIngrese elemento [1][1] de la matriz 2: ");
		                }
		            } else {
		                pantalla.append("\nIngrese elemento [" + (filaActual + 1) + "][" + (columnaActual + 1) + "] de la matriz " + (matrizActual + 1) + ": ");
		            }
		            return;
		        }

		       
		        if (estadoOperacion == 23){
		        escalar = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        matrices = new float[1][filas * columnas][1];
		        matrizActual = 0;
		        filaActual = 0;
		        columnaActual = 0;
		        pantalla.append("\nIngrese elemento [1][1] de la matriz: ");
		        estadoOperacion = 25;
		        return;
		        }
		        
		        if (estadoOperacion == 24){
			        columnas = Integer.parseInt(bufferNumero.trim());
			        bufferNumero = "";
			        pantalla.append("\nIngrese elemento [1][1] de la matriz 1: ");
			        matrices = new float[2][filas * columnas][1];
			        matrizActual = 0;
			        filaActual = 0;
			        columnaActual = 0;
			        estadoOperacion = 26;
			        return;
			    }
		        
		        if (estadoOperacion == 25) {
		        matrices[0][filaActual * columnas + columnaActual][0] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        columnaActual++;
		        if (columnaActual == columnas) {
		            columnaActual = 0;
		            filaActual++;
		        }
		        if (filaActual == filas) {
		            pantalla.append("\nResultado:\n");
		            for (int i = 0; i < filas; i++) {
		                for (int j = 0; j < columnas; j++) {
		                    float val = matrices[0][i * columnas + j][0];
		                    pantalla.append((val * escalar) + " ");
		                }
		                pantalla.append("\n");
		            }
		            estadoOperacion = 0;
		        } else {
		            pantalla.append("\nIngrese elemento [" + (filaActual + 1) + "][" + (columnaActual + 1) + "]: ");
		        }
		        return;
		        }
		        
		    if (estadoOperacion == 26)
		    {
		        matrices[matrizActual][filaActual * columnas + columnaActual][0] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        columnaActual++;
		        if (columnaActual == columnas) {
		            columnaActual = 0;
		            filaActual++;
		        }
		        if (filaActual == filas) {
		            matrizActual++;
		            filaActual = 0;
		            columnaActual = 0;
		            if (matrizActual == 2) {
		            	float[][] A = convertirA2D(matrices, 0, filas, columnasA);
		            	float[][] B = convertirA2D(matrices, 1, columnasA, columnas);
		                float[][] resultado = multiplicarMatrices(A, B);
		                pantalla.append("\nResultado:\n");
		                imprimirMatriz(resultado);
		                estadoOperacion = 0;
		            } else {
		                pantalla.append("\nIngrese elemento [1][1] de la matriz 2: ");
		            }
		        } else {
		            pantalla.append("\nIngrese elemento [" + (filaActual + 1) + "][" + (columnaActual + 1) + "] de la matriz " + (matrizActual + 1) + ": ");
		        }
		        return;
		    }

		    if (estadoOperacion == 100) {
		        filas = columnas = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";
		        pantalla.append("\nIngrese elemento [1][1]: ");
		        matrices = new float[1][filas * columnas][1];
		        matrizActual = 0;
		        filaActual = 0;
		        columnaActual = 0;
		        estadoOperacion = 101;
		        return;
		    }

		    if (estadoOperacion == 101) {
		        matrices[0][filaActual * columnas + columnaActual][0] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        columnaActual++;
		        if (columnaActual == columnas) {
		            columnaActual = 0;
		            filaActual++;
		        }
		        if (filaActual == filas) {
		            float[][] matriz = new float[filas][columnas];
		            for (int i = 0; i < filas; i++)
		                for (int j = 0; j < columnas; j++)
		                    matriz[i][j] = matrices[0][i * columnas + j][0];
		            float det = determinante(matriz);
		            pantalla.append("\nDeterminante: " + det);
		            estadoOperacion = 0;
		        } else {
		            pantalla.append("\nIngrese elemento [" + (filaActual + 1) + "][" + (columnaActual + 1) + "]: ");
		        }
		        return;
		    }

		    if (estadoOperacion == 110) {
		    	
		    
		        filas = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";
		        pantalla.append("\nIngrese cantidad de columnas: ");
		        estadoOperacion = 111;
		        return;
		    }
		    if (estadoOperacion == 111)
		    {
		        columnas = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";
		        pantalla.append("\nIngrese elemento [1][1]: ");
		        matrices = new float[1][filas * columnas][1];
		        matrizActual = 0;
		        filaActual = 0;
		        columnaActual = 0;
		        estadoOperacion = 112;
		        return;
		    }

		    if (estadoOperacion == 112) {
		        matrices[0][filaActual * columnas + columnaActual][0] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        columnaActual++;
		        if (columnaActual == columnas) {
		            columnaActual = 0;
		            filaActual++;
		        }
		        if (filaActual == filas) {
		            float[][] mat = convertirA2D(matrices, 0, filas, columnas);
		            float[][] t = transpuesta(mat);
		            pantalla.append("\nMatriz transpuesta:\n");
		            imprimirMatriz(t);
		            estadoOperacion = 0;
		        } else {
		            pantalla.append("\nIngrese elemento [" + (filaActual + 1) + "][" + (columnaActual + 1) + "]: ");
		        }
		        return;
		    }
		    if (estadoOperacion == 120)
		    {
		        filas = columnas = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";
		        pantalla.append("\nIngrese elemento [1][1]: ");
		        matrices = new float[1][filas * columnas][1];
		        matrizActual = 0;
		        filaActual = 0;
		        columnaActual = 0;
		        estadoOperacion = 121;
		        return;
		    }
		    if (estadoOperacion == 121) {
		        matrices[0][filaActual * columnas + columnaActual][0] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        columnaActual++;
		        if (columnaActual == columnas) {
		            columnaActual = 0;
		            filaActual++;
		        }
		        if (filaActual == filas) {
		            float[][] mat = convertirA2D(matrices, 0, filas, columnas);
		            float[][] inv = inversa(mat);
		            if (inv == null) {
		                pantalla.append("\nLa matriz no tiene inversa.\n");
		            } else {
		                pantalla.append("\nMatriz inversa:\n");
		                imprimirMatriz(inv);
		            }
		            estadoOperacion = 0;
		        } else {
		            pantalla.append("\nIngrese elemento [" + (filaActual + 1) + "][" + (columnaActual + 1) + "]: ");
		        }
		        return;
		    }
		    if (estadoOperacion == 130)
		    {
		        filas = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";
		        pantalla.append("\nIngrese cantidad de columnas: ");
		        estadoOperacion = 131;
		        return;
		    }
		    if (estadoOperacion == 131)
		    {
		        columnas = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";
		        pantalla.append("\nIngrese elemento [1][1] de la matriz A: ");
		        matrices = new float[2][filas * columnas][1];
		        matrizActual = 0;
		        filaActual = 0;
		        columnaActual = 0;
		        estadoOperacion = 132;
		        return;
		    }

		    if (estadoOperacion == 132)
		    {
		        matrices[matrizActual][filaActual * columnas + columnaActual][0] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        columnaActual++;
		        if (columnaActual == columnas) {
		            columnaActual = 0;
		            filaActual++;
		        }
		        if (filaActual == filas) {
		            matrizActual++;
		            filaActual = 0;
		            columnaActual = 0;
		            if (matrizActual == 2) {
		                float[][] A = convertirA2D(matrices, 0, filas, columnas);
		                float[][] B = convertirA2D(matrices, 1, columnas, columnas);
		                float[][] invB = inversa(B);
		                if (invB == null) {
		                    pantalla.append("\nLa segunda matriz no tiene inversa.\n");
		                } else {
		                    float[][] resultado = multiplicarMatrices(A, invB);
		                    pantalla.append("\nResultado de A * B^-1:\n");
		                    imprimirMatriz(resultado);
		                }
		                estadoOperacion = 0;
		            } else {
		                pantalla.append("\nIngrese elemento [1][1] de la matriz B: ");
		            }
		        } else {
		            pantalla.append("\nIngrese elemento [" + (filaActual + 1) + "][" + (columnaActual + 1) + "] de la matriz " + (matrizActual + 1) + ": ");
		        }
		        return;
		       
		    }
		    
		    if (estadoOperacion == 241) {
		        filasB = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";
		        pantalla.append("\nIngrese cantidad de columnas de la segunda matriz: ");
		        estadoOperacion = 242;
		        return;
		    }
		    
		    if (estadoOperacion == 242) {
		        columnas = Integer.parseInt(bufferNumero.trim());
		        bufferNumero = "";

		        if (columnasA != filasB) {
		            pantalla.append("\nError: columnas de A (" + columnasA + ") deben coincidir con filas de B (" + filasB + ").");
		            estadoOperacion = 0;
		            return;
		        }

		        pantalla.append("\nIngrese elemento [1][1] de la matriz 1: ");
		        matrices = new float[2][filas * columnasA][1];
		        matrizActual = 0;
		        filaActual = 0;
		        columnaActual = 0;
		        estadoOperacion = 26;
		        return;
		    }
		    
		    if (estadoOperacion == 300) {
		        coef2x2[contadorEcuacion2x2++] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        if (contadorEcuacion2x2 == 1) pantalla.append("\nIngrese b1: ");
		        else if (contadorEcuacion2x2 == 2) pantalla.append("\nIngrese c1: ");
		        else if (contadorEcuacion2x2 == 3) pantalla.append("\nIngrese a2: ");
		        else if (contadorEcuacion2x2 == 4) pantalla.append("\nIngrese b2: ");
		        else if (contadorEcuacion2x2 == 5) pantalla.append("\nIngrese c2: ");
		        else if (contadorEcuacion2x2 == 6) {
		            float a1 = coef2x2[0], b1 = coef2x2[1], c1 = coef2x2[2];
		            float a2 = coef2x2[3], b2 = coef2x2[4], c2 = coef2x2[5];
		            float det = a1 * b2 - a2 * b1;
		            if (det == 0) {
		                pantalla.append("\nNo tiene solucion unica.");
		            } else {
		                float x = (c1 * b2 - c2 * b1) / det;
		                float y = (a1 * c2 - a2 * c1) / det;
		                pantalla.append("\nSolucion:\n");
		                pantalla.append("x = " + x + "\n");
		                pantalla.append("y = " + y + "\n");
		            }
		            estadoOperacion = 0;
		            contadorEcuacion2x2 = 0;
		        }
		        return;
		    }
		    
		    if (estadoOperacion == 301) {
		        coef3x3[contadorEcuacion3x3++] = Float.parseFloat(bufferNumero.trim());
		        bufferNumero = "";
		        String[] etiquetas = {
		            "b1", "c1", "d1",
		            "a2", "b2", "c2", "d2",
		            "a3", "b3", "c3", "d3"
		        };
		        if (contadorEcuacion3x3 <= 11) {
		            pantalla.append("\nIngrese " + etiquetas[contadorEcuacion3x3 - 1] + ": ");
		        }
		        if (contadorEcuacion3x3 == 12) {
		            float a1 = coef3x3[0], b1 = coef3x3[1], c1 = coef3x3[2], d1 = coef3x3[3];
		            float a2 = coef3x3[4], b2 = coef3x3[5], c2 = coef3x3[6], d2 = coef3x3[7];
		            float a3 = coef3x3[8], b3 = coef3x3[9], c3 = coef3x3[10], d3 = coef3x3[11];

		            float D = a1*(b2*c3 - b3*c2) - b1*(a2*c3 - a3*c2) + c1*(a2*b3 - a3*b2);
		            if (D == 0) {
		                pantalla.append("\nNo tiene solucion unica.");
		            } else {
		                float Dx = d1*(b2*c3 - b3*c2) - b1*(d2*c3 - d3*c2) + c1*(d2*b3 - d3*b2);
		                float Dy = a1*(d2*c3 - d3*c2) - d1*(a2*c3 - a3*c2) + c1*(a2*d3 - a3*d2);
		                float Dz = a1*(b2*d3 - b3*d2) - b1*(a2*d3 - a3*d2) + d1*(a2*b3 - a3*b2);
		                float x = Dx / D;
		                float y = Dy / D;
		                float z = Dz / D;

		                pantalla.append("\nSolucion:\n");
		                pantalla.append("x = " + x + "\n");
		                pantalla.append("y = " + y + "\n");
		                pantalla.append("z = " + z + "\n");
		            }
		            estadoOperacion = 0;
		            contadorEcuacion3x3 = 0;
		        }
		        return;
		    }

		        switch (estadoOperacion) {
		        case 0:
	            	String texto = pantalla.getText();
	            	double resultado = 0;

	            	texto = texto.replace(',', '.'); 

	            	try {
	            	    if (texto.contains("+")) {
	            	        String[] partes = texto.split("\\+");
	            	        if (partes.length == 2) {
	            	            resultado = Double.parseDouble(partes[0].trim()) + Double.parseDouble(partes[1].trim());
	            	        } 
	            	        else {
	            	            pantalla.setText("Error: operacion mal formada");
	            	            return;
	            	        }
	            	    } 
	            	    else if (texto.contains("-")) {
	            	        int pos = -1;
	            	        for (int i = 1; i < texto.length(); i++) {
	            	            if (texto.charAt(i) == '-') {
	            	                pos = i;
	            	                break;
	            	            }
	            	        }

	            	        if (pos != -1) {
	            	            String parte1 = texto.substring(0, pos);
	            	            String parte2 = texto.substring(pos + 1);
	            	            resultado = Double.parseDouble(parte1.trim()) - Double.parseDouble(parte2.trim());
	            	        } 
	            	        else {
	            	            pantalla.setText("Error: operacion mal formada");
	            	            return;
	            	        }
	            	    }
	            	    else if (texto.contains("x")) {
	            	        String[] partes = texto.split("x");
	            	        if (partes.length == 2) {
	            	            resultado = Double.parseDouble(partes[0].trim()) * Double.parseDouble(partes[1].trim());
	            	        } 
	            	        else {
	            	            pantalla.setText("Error: operacion mal formada");
	            	            return;
	            	        }
	            	    }
	            	    else if (texto.contains("÷")) {
	            	        String[] partes = texto.split("÷");
	            	        if (partes.length == 2) {
	            	            resultado = Double.parseDouble(partes[0].trim()) / Double.parseDouble(partes[1].trim());
	            	        } 
	            	        else {
	            	            pantalla.setText("Error: operacion mal formada");
	            	            return;
	            	        }
	            	    }
	            	    else if (texto.contains("^")) {
	            	        String[] partes = texto.split("\\^");
	            	        if (partes.length == 2) {
	            	            resultado = Math.pow(Double.parseDouble(partes[0].trim()), Double.parseDouble(partes[1].trim()));
	            	        } 
	            	        else {
	            	            pantalla.setText("Error: operacion mal formada");
	            	            return;
	            	        }
	            	    }
	            	    else if (texto.contains("√")) {
	            	        String[] partes = texto.split("√");
	            	        if (partes.length == 2 && !partes[0].isEmpty() && !partes[1].isEmpty()) {
	            	            double indice = Double.parseDouble(partes[0].trim());
	            	            double radicando = Double.parseDouble(partes[1].trim());
	            	            resultado = Math.pow(radicando, 1.0 / indice);
	            	        } 
	            	        else {
	            	            pantalla.setText("Error: operacion mal formada");
	            	            return;
	            	        }
	            	    } 
	            	    else {
	            	        pantalla.append("\nOperacion no reconocida");
	            	        return;
	            	    }
	            	    
	            	    DecimalFormat formato = new DecimalFormat("#.########");
	            	    String resultadoFormateado = formato.format(resultado);
	            	    pantalla.setText(resultadoFormateado);
	            	    ultimoResultado = resultadoFormateado;
	            	    
	            	    pantalla.setText(String.valueOf(resultado));
	            	    ultimoResultado = String.valueOf(resultado);
	            	} catch (Exception ex) {
	            	    pantalla.setText("Error");
	            	}
	            	bufferNumero = "";
	                break;

		            case 1:  
		                cantVectores = Integer.parseInt(bufferNumero.trim());
		                bufferNumero = "";
		                if (cantVectores < 2) {
		                    pantalla.append("\nDebe ingresar al menos 2 vectores");
		                    estadoOperacion = 0;
		                    break;
		                }
		                pantalla.append("\nIngrese cantidad de elementos por vector: ");
		                estadoOperacion = 2;
		                break;

		            case 2:  
		                cantElementos = Integer.parseInt(bufferNumero.trim());
		                bufferNumero = "";
		                vectores = new float[cantVectores][cantElementos];
		                vectorActual = 0;
		                elementoActual = 0;
		                pantalla.append("\nIngrese elemento 1 del vector 1: ");
		                estadoOperacion = 3;
		                break;

		            case 3: 
		                vectores[vectorActual][elementoActual] = Float.parseFloat(bufferNumero.trim());
		                bufferNumero = "";
		                elementoActual++;
		                if (elementoActual == cantElementos) {
		                    elementoActual = 0;
		                    vectorActual++;
		                }
		                if (vectorActual == cantVectores) {
		                    float[] resultadoVector = new float[cantElementos];
		                    if (operacionVector == 1) {
		                        for (int i = 0; i < cantVectores; i++)
		                            for (int j = 0; j < cantElementos; j++)
		                                resultadoVector[j] += vectores[i][j];
		                        pantalla.append("\nResultado de la suma: ");
		                    } else if (operacionVector == 2) {
		                        for (int j = 0; j < cantElementos; j++)
		                            resultadoVector[j] = vectores[0][j];
		                        for (int i = 1; i < cantVectores; i++)
		                            for (int j = 0; j < cantElementos; j++)
		                                resultadoVector[j] -= vectores[i][j];
		                        pantalla.append("\nResultado de la resta: ");
		                    }
		                    pantalla.append("(");
		                    for (int j = 0; j < cantElementos; j++) {
		                        pantalla.append(String.valueOf(resultadoVector[j]));
		                        if (j < cantElementos - 1)
		                            pantalla.append(", ");
		                    }
		                    pantalla.append(")\n");
		                    estadoOperacion = 0;
		                } else {
		                    pantalla.append("\nIngrese elemento " + (elementoActual + 1) + " del vector " + (vectorActual + 1) + ": ");
		                }
		                break;

		            case 4: 
		                cantElementos = Integer.parseInt(bufferNumero.trim());
		                bufferNumero = "";
		                vectores = new float[1][cantElementos];
		                vectorActual = 0;
		                elementoActual = 0;
		                pantalla.append("\nIngrese elemento 1 del vector: ");
		                estadoOperacion = 5;
		                break;

		            case 5:  
		                vectores[0][elementoActual] = Float.parseFloat(bufferNumero.trim());
		                bufferNumero = "";
		                elementoActual++;
		                if (elementoActual == cantElementos) {
		                    pantalla.append("\nIngrese escalar: ");
		                    estadoOperacion = 6;
		                } else {
		                    pantalla.append("\nIngrese elemento " + (elementoActual + 1) + ": ");
		                }
		                break;

		            case 6:  
		                escalar = Float.parseFloat(bufferNumero.trim());
		                bufferNumero = "";
		                float[] resultadoEscalar = new float[cantElementos];
		                for (int i = 0; i < cantElementos; i++)
		                    resultadoEscalar[i] = vectores[0][i] * escalar;
		                pantalla.append("\nResultado del producto por escalar: (");
		                for (int j = 0; j < cantElementos; j++) {
		                    pantalla.append(String.valueOf(resultadoEscalar[j]));
		                    if (j < cantElementos - 1)
		                        pantalla.append(", ");
		                }
		                pantalla.append(")\n");
		                estadoOperacion = 0;
		                break;

		            case 7:  
		                cantElementos = Integer.parseInt(bufferNumero.trim());
		                bufferNumero = "";
		                vectores = new float[2][cantElementos];
		                vectorActual = 0;
		                elementoActual = 0;
		                pantalla.append("\nIngrese elemento 1 del vector 1: ");
		                estadoOperacion = 8;
		                break;

		            case 8:  
		                vectores[vectorActual][elementoActual] = Float.parseFloat(bufferNumero.trim());
		                bufferNumero = "";
		                elementoActual++;
		                if (elementoActual == cantElementos) {
		                    elementoActual = 0;
		                    vectorActual++;
		                }
		                if (vectorActual == 2) {
		                    float productoEscalar = 0;
		                    for (int i = 0; i < cantElementos; i++)
		                        productoEscalar += vectores[0][i] * vectores[1][i];
		                    pantalla.append("\nResultado del producto escalar: " + productoEscalar + "\n");
		                    estadoOperacion = 0;
		                } else {
		                    pantalla.append("\nIngrese elemento " + (elementoActual + 1) + " del vector " + (vectorActual + 1) + ": ");
		                }
		                break;

		            case 9:  
		                cantElementos = Integer.parseInt(bufferNumero.trim());
		                bufferNumero = "";
		                if (cantElementos != 3) {
		                    pantalla.append("\nError: el producto vectorial solo se puede hacer con vectores de 3 elementos.");
		                    estadoOperacion = 0;
		                    break;
		                }
		                vectores = new float[2][3];
		                vectorActual = 0;
		                elementoActual = 0;
		                pantalla.append("\nIngrese elemento 1 del vector 1: ");
		                estadoOperacion = 10;
		                break;

		            case 10:  
		                vectores[vectorActual][elementoActual] = Float.parseFloat(bufferNumero.trim());
		                bufferNumero = "";
		                elementoActual++;
		                if (elementoActual == 3) {
		                    elementoActual = 0;
		                    vectorActual++;
		                }
		                if (vectorActual == 2) {
		                    float[] v1 = vectores[0];
		                    float[] v2 = vectores[1];
		                    float[] productoVectorial = new float[3];
		                    productoVectorial[0] = v1[1]*v2[2] - v1[2]*v2[1];
		                    productoVectorial[1] = v1[2]*v2[0] - v1[0]*v2[2];
		                    productoVectorial[2] = v1[0]*v2[1] - v1[1]*v2[0];
		                    pantalla.append("\nProducto vectorial: (");
		                    for (int i = 0; i < 3; i++) {
		                        pantalla.append(String.valueOf(productoVectorial[i]));
		                        if (i < 2) pantalla.append(", ");
		                    }
		                    pantalla.append(")\n");
		                    estadoOperacion = 0;
		                } else {
		                    pantalla.append("\nIngrese elemento " + (elementoActual + 1) + " del vector " + (vectorActual + 1) + ": ");
		                }
		                break;
		                
		            case 110:
		                pantalla.append("\n[Transpuesta] Funcionalidad pendiente de implementacion.");
		                estadoOperacion = 0;
		                break;

		            case 120:
		                pantalla.append("\n[Inversa] Funcionalidad pendiente de implementacion.");
		                estadoOperacion = 0;
		                break;

		            case 130:
		                pantalla.append("\n[División de matrices] Funcionalidad pendiente de implementacion.");
		                estadoOperacion = 0;
		                break;

		            default:
		                pantalla.append("\nEstado no reconocido");
		                estadoOperacion = 0;
		                break;
		        }

		    } catch (Exception ex) {
		        pantalla.append("\nError en la entrada");
		        bufferNumero = "";
		        estadoOperacion = 0;
		    }
		});




		botonAns = new JButton("Ans");
		botonAns.setForeground(Color.WHITE);
		botonAns.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonAns.setBackground(new Color(109, 7, 26));
		botonAns.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonAns.setBounds(303, 213, 57, 57);
		contentPane.add(botonAns);
		botonAns.addActionListener(e -> {
		    if (!ultimoResultado.isEmpty()) {
		        bufferNumero += ultimoResultado;
		        pantalla.append(ultimoResultado);
		    } else {
		        pantalla.append("\nNo hay resultado previo");
		    }
		});

		botonAc = new JButton("AC");
		botonAc.setForeground(Color.WHITE);
		botonAc.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonAc.setBackground(new Color(109, 7, 26));
		botonAc.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonAc.setBounds(232, 247, 57, 23);
		contentPane.add(botonAc);
		botonAc.addActionListener(e -> {
			pantalla.setText("");
			bufferNumero = "";
			estadoOperacion = 0;
		});

		botonPunto = new JButton(".");
		botonPunto.setForeground(Color.WHITE);
		botonPunto.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonPunto.setBackground(new Color(109, 7, 26));
		botonPunto.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonPunto.setBounds(20, 247, 57, 23);
		contentPane.add(botonPunto);
		botonPunto.addActionListener(e -> {
		    String texto = pantalla.getText();

		    int pos = Math.max(
		        Math.max(texto.lastIndexOf("+"), texto.lastIndexOf("-")),
		        Math.max(texto.lastIndexOf("x"), texto.lastIndexOf("÷"))
		    );
		    pos = Math.max(pos, texto.lastIndexOf("^"));
		    pos = Math.max(pos, texto.lastIndexOf("√"));

		    String actual = (pos == -1) ? texto : texto.substring(pos + 1);
		    
		    if (!actual.contains(".")) {
		        pantalla.append(".");
		    }
		});

		botonSuma = new JButton("+");
		botonSuma.setForeground(Color.WHITE);
		botonSuma.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonSuma.setBackground(new Color(109, 7, 26));
		botonSuma.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonSuma.setBounds(370, 179, 57, 23);
		botonSuma.addActionListener(e -> pantalla.append("+"));
		contentPane.add(botonSuma);

		JButton botonResta = new JButton("-");
		botonResta.setForeground(Color.WHITE);
		botonResta.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonResta.setBackground(new Color(109, 7, 26));
		botonResta.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonResta.setBounds(434, 179, 57, 23);
		botonResta.addActionListener(e -> pantalla.append("-"));
		contentPane.add(botonResta);

		JButton botonDivision = new JButton("÷");
		botonDivision.setForeground(Color.WHITE);
		botonDivision.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonDivision.setBackground(new Color(109, 7, 26));
		botonDivision.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonDivision.setBounds(370, 213, 57, 23);
		botonDivision.addActionListener(e -> pantalla.append("÷"));
		contentPane.add(botonDivision);

		JButton botonMultiplicacion = new JButton("x");
		botonMultiplicacion.setForeground(Color.WHITE);
		botonMultiplicacion.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonMultiplicacion.setBackground(new Color(109, 7, 26));
		botonMultiplicacion.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonMultiplicacion.setBounds(434, 213, 57, 23);
		botonMultiplicacion.addActionListener(e -> pantalla.append("x"));
		contentPane.add(botonMultiplicacion);
		
		JButton botonRadicacion = new JButton("ⁿ√×");
		botonRadicacion.setForeground(Color.WHITE);
		botonRadicacion.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonRadicacion.setBackground(new Color(109, 7, 26));
		botonRadicacion.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonRadicacion.setBounds(370, 247, 57, 23);
		botonRadicacion.addActionListener(e -> pantalla.setText(pantalla.getText() + "√"));
		contentPane.add(botonRadicacion);
		
		JButton botonPotenciacion = new JButton("xⁿ");
		botonPotenciacion.setForeground(Color.WHITE);
		botonPotenciacion.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonPotenciacion.setBackground(new Color(109, 7, 26));
		botonPotenciacion.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonPotenciacion.setBounds(434, 247, 57, 23);
		botonPotenciacion.addActionListener(e -> pantalla.setText(pantalla.getText() + "^"));
		contentPane.add(botonPotenciacion);

		label_operaciones_basicas = new JLabel("Operaciones básicas");
		label_operaciones_basicas.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		label_operaciones_basicas.setBounds(370, 164, 130, 14);
		contentPane.add(label_operaciones_basicas);

		botonVectores = new JButton("Vectores");
		botonVectores.setForeground(Color.WHITE);
		botonVectores.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonVectores.setBackground(new Color(109, 7, 26));
		botonVectores.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonVectores.setBounds(370, 42, 121, 23);
		contentPane.add(botonVectores);
		botonVectores.addActionListener(e -> {
			pantalla.setText("Menu vectores:\n1. Suma de vectores\n2. Resta de vectores\n3. Multiplicacion de escalar por vector\n4. Producto escalar\n5. Producto vectorial\nSeleccione opcion: ");
			bufferNumero = "";
			estadoOperacion = -1;
		});

		lblOperacionesVectores = new JLabel("Operaciones vectores");
		lblOperacionesVectores.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		lblOperacionesVectores.setBounds(370, 32, 130, 14);
		contentPane.add(lblOperacionesVectores);

		botonMatrices = new JButton("Matrices");
		botonMatrices.setForeground(Color.WHITE);
		botonMatrices.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonMatrices.setBackground(new Color(109, 7, 26));
		botonMatrices.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonMatrices.setBounds(370, 80, 121, 23);
		contentPane.add(botonMatrices);
		botonMatrices.addActionListener(e -> {
		    pantalla.setText("Menu matrices:\n1. Suma de matrices\n2. Resta de matrices\n3. Multiplicacion por escalar\n4. Multiplicacion de matrices\n5. Transpuesta de una matriz\n6. Determinante de una matriz\n7. Inversa de una matriz\n8. Division de matrices\nSeleccione opcion: ");
		    bufferNumero = "";
		    estadoOperacion = -2;
		});
		lblOperacionesMatrices = new JLabel("Operaciones matrices");
		lblOperacionesMatrices.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		lblOperacionesMatrices.setBounds(370, 67, 130, 14);
		contentPane.add(lblOperacionesMatrices);
		
		botonEcuaciones = new JButton("Ecuaciones");
		botonEcuaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pantalla.setText("Menu Ecuaciones:\n1. Sistema de 2x2\n2. Sistema de 3x3\nSeleccione opcion: ");
				bufferNumero = "";
				estadoOperacion = -3;
			}
		});
		botonEcuaciones.setForeground(Color.WHITE);
		botonEcuaciones.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		botonEcuaciones.setBorder(BorderFactory.createMatteBorder(2, 2, 6, 6, new Color(60, 0, 10)));
		botonEcuaciones.setBackground(new Color(109, 7, 26));
		botonEcuaciones.setBounds(370, 120, 121, 23);
		contentPane.add(botonEcuaciones);
		
		JLabel lblOperacionesEcuaciones = new JLabel("Operaciones Ecuaciones");
		lblOperacionesEcuaciones.setFont(new Font("Sylfaen", Font.ITALIC, 11));
		lblOperacionesEcuaciones.setBounds(370, 107, 130, 14);
		contentPane.add(lblOperacionesEcuaciones);
	}
}