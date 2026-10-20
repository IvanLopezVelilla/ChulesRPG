package main;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;

	public static void main(String[] args) {
		/*
		 * MAIN
		 * Creates the game window and calls the GameLoop, which starts the game
		 */
		configurarOptimizacionesMultiplataforma();
		
		//Window configuration
		window=new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true); //I will try in the future to make the game window properly resizable
		window.setTitle("ChulesRPG");
		
		GamePanel gamePanel=new GamePanel();
		window.add(gamePanel);
		
		gamePanel.config.loadConfig();
		
		if(gamePanel.fullScreen) {
			
			window.setUndecorated(true);
			
		}
		
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.setupGame();
		gamePanel.start();
		

		

	}
	
	//TENGO QUE TRADUCIR ESTO AL INGLÉS
	//Funciones de configuración del juego
	//Ajusta la configuración del juego según el SO
	private static void configurarOptimizacionesMultiplataforma() {
		//Detectar sistema operativo
		String nombreSO=System.getProperty("os.name").toLowerCase();
//		System.out.println("=== INICIANDO CONFIGURACIÓN ===");
//		System.out.println("Sistema operativo: "+nombreSO);
		
		//Configuraciones globales
//		System.setProperty("awt.useSystemAAFontSetting", "on");
//		System.setProperty("swing.aatext", "true");
		
		//Configuraciones específicas
		if(nombreSO.contains("linux")) {
//			System.out.println("Configurando para LINUX...");
			configurarLinux();
			
		}else if(nombreSO.contains("windows")) {
//			System.out.println("Configurando para WINDOWS");
			configurarWindows();
			
		}else{
//			System.out.println("Sistema no reconocido");
		}
		
//		System.out.println("=== CONFIGURACIÓN COMPLETADA ===\n");
	}
	
	
	
	//Establece la configuración específica de Linux
	private static void configurarLinux(){
		System.setProperty("sun.java2d.opengl", "True");
//		System.out.println("-OpenGL activado");
		
		System.setProperty("sun.java2d.xrender", "True");
//		System.out.println("-XRender activado");
		
		System.setProperty("sun.java2d.accthreshold", "0");
//		System.out.println("-Aceleración por hardware forzada");
		
		System.setProperty("sun.java2d.noddraw", "true");
//		System.out.println("-DirectDraw desactivado");
	}
	
	
	//Establece la configuración específica de Windows
	private static void configurarWindows() {
        System.setProperty("sun.java2d.d3d", "True");
        System.out.println("-Direct3D activado");
        
        System.setProperty("sun.java2d.ddforcevram", "true");
        System.out.println("-Memoria de video forzada");
        
        System.setProperty("sun.java2d.noddraw", "false");
        System.out.println("-DirectDraw activado");
	}

}
