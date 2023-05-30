package pong.menu;

import java.awt.Color;
import java.awt.Font;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import pong.Renderer;

public class Menu {
    private TextRenderer textRenderer;

    private void desenhaTexto(int xPosicao, int yPosicao, Color cor, String frase, int size) {
    	textRenderer = new TextRenderer(new Font("Roboto", Font.PLAIN, size));
        textRenderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
    }

    public void jogo(String frase) {
        this.desenhaTexto(15, 560, Color.BLACK, frase, 25);
    }
    
    public void start(GL2 gl) {
    	this.iluminacao(gl);
    	this.desenhaTexto(25, 550, Color.GREEN, "Regras e Comandos:", 30);
    	this.desenhaTexto(50, 520, Color.LIGHT_GRAY, "A cada rebatida na bola a pontuação será", 20);
    	this.desenhaTexto(50, 500, Color.LIGHT_GRAY, "incrementada 25 pontos, até atingir 200 pontos", 20);
    	this.desenhaTexto(50, 480, Color.LIGHT_GRAY, "Se o jogador atingir 200 pontos a 2ª fase será liberada", 20);
    	this.desenhaTexto(50, 460, Color.LIGHT_GRAY, "Caso o jogador atinja os 400 pontos, vencerá o jogo!", 20);
    	this.desenhaTexto(50, 440, Color.LIGHT_GRAY, "No entanto, se o jogador errar 5 rebatidas, o jogo termina!", 20);
    	this.desenhaTexto(100, 350, Color.GREEN, "ENTER: Start", 20);
    	this.desenhaTexto(100, 300, Color.GREEN, "P: Pause",20);
    	this.desenhaTexto(100, 250, Color.GREEN, "ESC: Stop", 20);
    	this.desenhaTexto(100, 200, Color.GREEN, "🠔➝: Deslocamento do Bastão", 20);
        this.desenhaTexto(180, 100, Color.LIGHT_GRAY, "Aperte Enter para começar", 20);
    }

    public void segundaFase() {
        this.desenhaTexto(160, 300, Color.LIGHT_GRAY, "Segunda Fase", 40);
    }

    public void pontos(int pontos) {
        this.desenhaTexto(280, 555, Color.YELLOW, pontos+"", 30);
    }

    public void pause() {
        this.desenhaTexto(225, 300, Color.BLUE, "PAUSE", 40);
    }

    public void gameOver() {
        this.desenhaTexto(175, 300, Color.RED, "Você Perdeu :(", 40);
    }

    public void venceu() {
        this.desenhaTexto(175, 300, Color.GREEN, "Você venceu!", 40);
    }
    
    private void iluminacao(GL2 gl) {
        // Luz especular
    	gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        //  Definindo a posição da fonte de luz:
        float[] lightPosition = {1, 1, 0.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        
        float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
        float[] diffuse = {1.0f, 0.0f, 0.0f, 1.0f};
        float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};
        float shininess = 10.0f;

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
    }
}
