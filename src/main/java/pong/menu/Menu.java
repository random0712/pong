package pong.menu;

import com.jogamp.opengl.util.awt.TextRenderer;
import pong.Renderer;
import pong.cena.Cena;

import java.awt.*;

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
    
    public void start() {
        this.desenhaTexto(140, 300, Color.LIGHT_GRAY, "Aperte Enter para começar", 30);
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
}
