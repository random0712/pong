package pong.cena;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import pong.menu.Menu;
import pong.textura.Textura;

import java.io.File;
import java.io.IOException;

public class Cena implements GLEventListener, KeyListener {
    private Menu menu;
    public static final String texturaImg = "src/imagens/fundo.jpg";

    private int opcao = 0;
    private boolean iniJogo = true;
    private boolean pausar = true;
    
    public int tonalizacao = GL2.GL_SMOOTH; 

    private int resAtual = 0;
    public static int pointSegundaFase = 200;
    public static int pointBatida = 25;
    public static int PointTotal = 400;
    private boolean segundaFase = true;

    private float moverY = 0;
    private float moverX = 0;
    private float velocidade = 0.012f;

    private float bordaSup = 0.85f;
    private float bordaDir = 0.87f;
    private float bordaEsq = -1;

    private float raqueteX1 = -0.2f;
    private float raqueteX2 = 0.2f;
    private float raqueteY1 = -0.8f;
    private float raqueteY2 = -0.9f;

    private float raquetePontaEsq = raqueteX1;
    private float raquetePontaDir = raqueteX2;
    private float moverRaqueteX = 0;

    private float raqueteMeio = 0;
    private float obstaculoMeio = 0;

    private float obstaculo2X1 = -0.5f;
    private float obstaculo2X2 = 0.5f;
    private float obstaculo2Y1 = 0.4f;
    private float obstaculo2Y2 = 0.3f;

    private float obstaculoPontaEsq = obstaculo2X1;
    private float obstaculoPontaDir = obstaculo2X2;

    private float meioDaTela = 0.0f;
    private float meioDaTelaLadoEsq = -0.5f;
    private float meioDaTelaLadoDir = 0.5f;

    private float cX = 0.0f;
    private float cY = 0.0f;
    private float rX = 0.05f;
    private float rY = 0.075f;
    private float posicaoBolaX = cX + rX;
    private float posicaoBolaY = cY + rY;

    private boolean subirVetical = false;
    private boolean descerVetical = false;
    private boolean subirDiagonalDir = false;
    private boolean subirDiagonalEsq = false;
    private boolean descerDiagonalDir = false;
    private boolean descerDiagonalEsq = false;

    public static int vida = 5;

    private float posicaoVidaEsqX = 0.5f;
    private float posicaoVidaEsqY = 0.9f;
    private float posicaoVidaZ = 0.3f;

    private Texture backgroundTexture;

    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);

        //Liga iluminacao
//        gl.glEnable(GL2.GL_LIGHT1);
//        gl.glEnable(GL2.GL_LIGHTING);

        new Textura(1);

        //Habilita as cores do objeto 3D
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        gl.glEnable(GL2.GL_DEPTH_TEST); // Habilita o teste de profundidade
        gl.glDepthFunc(GL2.GL_LEQUAL); // Define a função de teste de profundidade

        try {
            File imageFile = new File("src/imagens/tennis.jpg"); // Caminho da imagem de fundo
            backgroundTexture = TextureIO.newTexture(imageFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // define a cor da janela (R, G, B, alpha)
        gl.glColor3f(1.0f,1.0f,1.0f);

        GLUT glut = new GLUT();
        
//
        // limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1, 1, -1, 1, -1, 1);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        // Desenhar um quadrado preenchendo toda a janela
        gl.glEnable(GL2.GL_TEXTURE_2D);
        backgroundTexture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-1, -1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, -1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-1, 1);
        gl.glEnd();
        gl.glDisable(GL2.GL_TEXTURE_2D);
    

        switch (opcao) {
	        case 0:
	            this.menu = new Menu();
	            this.menu.start();
	            break;
            case 2:
                this.menu = new Menu();
                double lim = 2 * Math.PI;

                gl.glPushMatrix();

                if (iniJogo) {
                    this.gameOver();
                    this.inicio(gl);
                }
                this.gameOver();

                if (resAtual >= pointSegundaFase) {
                    this.menu.jogo("Segunda Fase");
                } else {
                    this.menu.jogo("Primeira Fase");
                }

                if (resAtual == pointSegundaFase && segundaFase) {
                    opcao = 5;
                    this.reset(false);
                    segundaFase = false;
                    descerVetical = true;
                }

                posicaoBolaY = -(cY + rY) + moverY;
                posicaoBolaX = -(cX + rX) + moverX;

                if (resAtual >= pointSegundaFase) {
                    velocidade = 0.015f;

                    obstaculoMeio = (obstaculoPontaEsq + obstaculoPontaDir) / 2;
                    float BOLA_X = (posicaoBolaX + rX);
                    float BASTAO_Y2 = (obstaculo2Y2 - 0.15f);

                    if (subirDiagonalDir || subirDiagonalEsq || subirVetical) {

                        obstaculo2X1 = -0.5f;
                        obstaculo2X2 = 0.5f;

                        obstaculoPontaEsq = obstaculo2X1;
                        obstaculoPontaDir = obstaculo2X2;

                        if (posicaoBolaY >= BASTAO_Y2 && posicaoBolaY < obstaculo2Y1
                                && BOLA_X >= obstaculoPontaEsq
                                && BOLA_X <= obstaculoPontaDir) {


                            if (posicaoBolaX == -rX || posicaoBolaX == obstaculoMeio && subirVetical) {
                                descerVetical = true;
                                subirDiagonalEsq = false;
                                subirDiagonalDir = false;
                                subirVetical = false;
                            }

                            if (subirDiagonalEsq) {
                                descerDiagonalEsq = true;
                                subirDiagonalEsq = false;
                                subirDiagonalDir = false;
                                subirVetical = false;
                            }
                            if (subirDiagonalDir) {
                                descerDiagonalDir = true;
                                subirDiagonalEsq = false;
                                subirDiagonalDir = false;
                                subirVetical = false;
                            }
                        }
                    }

                    if (descerDiagonalDir || descerDiagonalEsq) {

                        obstaculo2X1 = -0.3f;
                        obstaculo2X2 = 0.3f;

                        obstaculoPontaEsq = obstaculo2X1;
                        obstaculoPontaDir = obstaculo2X2;

                        if (posicaoBolaY <= obstaculo2Y1 && posicaoBolaY > obstaculo2Y2
                                && BOLA_X >= obstaculoPontaEsq
                                && BOLA_X <= obstaculoPontaDir) {


                            if (descerDiagonalDir) {
                                subirDiagonalDir = true;
                                descerVetical = false;
                                descerDiagonalEsq = false;
                                descerDiagonalDir = false;
                            }
                            if (descerDiagonalEsq) {
                                subirDiagonalEsq = true;
                                descerVetical = false;
                                descerDiagonalEsq = false;
                                descerDiagonalDir = false;
                            }
                        }
                    }

                    if (resAtual == PointTotal) {
                        opcao = 6;
                        this.reset(true);
                        resAtual = 0;
                        vida = 5;
                        segundaFase = true;
                    }
                }


                if (posicaoBolaY <= raqueteY1) {
                    descerDiagonalEsq = false;
                    descerDiagonalDir = false;
                    descerVetical = false;

                    resAtual += pointBatida;

                    raqueteMeio = (raquetePontaEsq + raquetePontaDir) / 2;

                    if (posicaoBolaX == -rX && raqueteMeio == meioDaTela || posicaoBolaX == raqueteMeio) {
                        subirVetical = true;
                    }

                    if (posicaoBolaX > raqueteMeio) {
                        if (raqueteMeio < meioDaTela && raqueteMeio >= meioDaTelaLadoEsq) {
                            subirDiagonalDir = true;
                        }

                        if (raqueteMeio < meioDaTela && raqueteMeio < meioDaTelaLadoEsq) {
                            subirDiagonalEsq = true;
                        }

                        if (raqueteMeio > meioDaTela && raqueteMeio <= meioDaTelaLadoDir) {
                            subirDiagonalDir = true;
                        }

                        if (raqueteMeio > meioDaTela && raqueteMeio > meioDaTelaLadoDir) {
                            subirDiagonalEsq = true;
                        }
                    }

                    if (posicaoBolaX < raqueteMeio) {
                        if (raqueteMeio < meioDaTela && raqueteMeio > meioDaTelaLadoEsq) {
                            subirDiagonalEsq = true;
                        }

                        if (raqueteMeio < meioDaTela && raqueteMeio <= meioDaTelaLadoEsq) {
                            subirDiagonalDir = true;
                        }

                        if (raqueteMeio > meioDaTela && raqueteMeio <= meioDaTelaLadoDir) {
                            subirDiagonalEsq = true;
                        }

                        if (raqueteMeio > meioDaTela && raqueteMeio > meioDaTelaLadoDir) {
                            subirDiagonalDir = true;
                        }
                    }
                }

                if (subirDiagonalEsq) {
                    this.subirEsquerda(gl);
                }

                if (subirDiagonalDir) {
                    this.subirDireita(gl);
                }

                if (descerDiagonalEsq) {
                    this.descerEsquerda(gl);
                }

                if (descerDiagonalDir) {
                    this.descerDireita(gl);
                }

                if (subirVetical) {
                    this.subirReto(gl);
                }

                if (descerVetical) {
                    this.descerReto(gl);
                }
                gl.glEnable(GL2.GL_LIGHTING);
                gl.glEnable(GL2.GL_LIGHT0);
                gl.glEnable(GL2.GL_COLOR_MATERIAL);
                
//                Defina a posição da fonte de luz:
                	float[] lightPosition = {1, 1, 0.0f, 1.0f}; // Posição da luz no centro da bola (z = 0)
                	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

//                	Defina as propriedades do material para a bola:

                	float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
                	float[] diffuse = {1.0f, 0.0f, 0.0f, 1.0f};
                	float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};
                	float shininess = 10.0f;

                	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
                	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
                	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
                	gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
                
                
                gl.glBegin(GL2.GL_POLYGON);
                gl.glColor3f(1, 1, 0);
                for (float i = 0; i < lim; i += 0.01) {
//                    gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
                	  float normalX = (float) Math.cos(i); // Normal x (mesmo que o raio)
                	    float normalY = (float) Math.sin(i); // Normal y (mesmo que o raio)

                	    gl.glNormal3f(normalX, normalY, 0.0f); // Especifica a normal do vértice
                	    gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
                }

                gl.glEnd();
                gl.glPopMatrix();

                //BASTAO
                gl.glPushMatrix();
                gl.glColor3f(0.8f, 0.8f, 0.8f);
                gl.glTranslatef(moverRaqueteX, 0, 0);
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2d(raqueteX1, raqueteY1);
                gl.glVertex2d(raqueteX2, raqueteY1);
                gl.glVertex2d(raqueteX2, raqueteY2);
                gl.glVertex2d(raqueteX1, raqueteY2);


                gl.glEnd();
                gl.glPopMatrix();

                if (resAtual >= pointSegundaFase) {
                    // BASTAO 2
                    gl.glPushMatrix();
                    gl.glColor3f(1, 1, 1);
                    gl.glBegin(GL2.GL_QUADS);
                    gl.glVertex2d(obstaculo2X1, obstaculo2Y1);
                    gl.glVertex2d(obstaculo2X2, obstaculo2Y1);
                    gl.glVertex2d(obstaculo2X2, obstaculo2Y2);
                    gl.glVertex2d(obstaculo2X1, obstaculo2Y2);
                    gl.glEnd();
                    gl.glPopMatrix();
                }

                float incr = 0;
                for (int i = 0; i < vida; i += 1) {
                    desenhaVida(gl, glut, incr);
                    incr += 0.1f;
                }
                this.menu.pontos(resAtual);
                break;
            case 3:
                this.menu = new Menu();
                this.menu.pause();
                break;
            case 4:
                this.menu = new Menu();
                this.menu.gameOver();
                break;
            case 5:
                this.menu = new Menu();
                this.menu.segundaFase();
                break;
            case 6:
                this.menu = new Menu();
                this.menu.venceu();
                break;
        }

        gl.glFlush();
    }

    public void desenhaVida(GL2 gl, GLUT glut, float incr) {
        gl.glPushMatrix();
        gl.glColor3f(1, 1, 1); // Cor branca para a vida

        // Desenhar o corpo da vida (cogumelo)
        gl.glPushMatrix();
        gl.glTranslatef(posicaoVidaEsqX + incr, posicaoVidaEsqY, posicaoVidaZ);

        float tamanho = 0.15f;

        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2f(-0.3f * tamanho, 0);
        gl.glVertex2f(0.3f * tamanho, 0);
        gl.glVertex2f(0.3f * tamanho, tamanho);
        gl.glVertex2f(0, tamanho * 2);
        gl.glVertex2f(-0.3f * tamanho, tamanho);
        gl.glEnd();

        gl.glPopMatrix();

        // Desenhar os olhos da vida (pontos pretos)
        gl.glPushMatrix();
        gl.glTranslatef(posicaoVidaEsqX + incr, posicaoVidaEsqY + tamanho * 1.3f, posicaoVidaZ);

        float tamanhoOlho = tamanho * 0.2f;

        gl.glColor3f(0, 0, 0); // Cor preta para os olhos

        glut.glutSolidSphere(tamanhoOlho, 10, 10);

        gl.glTranslatef(tamanho * 0.3f, 0, 0);
        glut.glutSolidSphere(tamanhoOlho, 10, 10);

        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    private void subirDireita(GL2 gl) {
        gl.glTranslatef(moverX += velocidade, moverY += velocidade, 0);
        if (posicaoBolaX >= bordaDir) {
            subirDiagonalEsq = true;
            subirDiagonalDir = false;
            subirVetical = false;
            descerDiagonalEsq = false;
            descerDiagonalDir = false;
            descerVetical = false;
        }

        if (posicaoBolaY >= bordaSup) {
            descerDiagonalDir = true;
            subirVetical = false;
            subirDiagonalDir = false;
            subirDiagonalEsq = false;
            descerDiagonalEsq = false;
            descerVetical = false;
        }
    }

    private void subirEsquerda(GL2 gl) {
        gl.glTranslatef(moverX -= velocidade, moverY += velocidade, 0);
        if (posicaoBolaX <= bordaEsq) {
            subirDiagonalDir = true;
            subirDiagonalEsq = false;
            subirVetical = false;
            descerVetical = false;
            descerDiagonalDir = false;
            descerDiagonalEsq = false;
        }

        if (posicaoBolaY >= bordaSup) {
            descerDiagonalEsq = true;
            subirDiagonalEsq = false;
            subirDiagonalDir = false;
            subirVetical = false;
            descerDiagonalDir = false;
            descerVetical = false;
        }
    }

    private void descerEsquerda(GL2 gl) {
        gl.glTranslatef(moverX -= velocidade, moverY -= velocidade, 0);
        if (posicaoBolaX <= bordaEsq) {
            descerDiagonalDir = true;
            descerDiagonalEsq = false;
            descerVetical = false;
            subirVetical = false;
            subirDiagonalDir = false;
            subirDiagonalEsq = false;
        }
    }

    private void descerDireita(GL2 gl) {
        gl.glTranslatef(moverX += velocidade, moverY -= velocidade, 0);
        if (posicaoBolaX >= bordaDir) {
            descerDiagonalEsq = true;
            descerDiagonalDir = false;
            descerVetical = false;
            subirVetical = false;
            subirDiagonalDir = false;
            subirDiagonalEsq = false;
        }
    }

    private void subirReto(GL2 gl) {
        gl.glTranslatef(0, moverY += velocidade, 0);
        if (posicaoBolaY >= bordaSup) {
            descerVetical = true;
            subirVetical = false;
            subirDiagonalDir = false;
            subirDiagonalEsq = false;
            descerDiagonalDir = false;
            descerDiagonalEsq = false;
        }
    }

    private void descerReto(GL2 gl) {
        gl.glTranslatef(0, moverY -= velocidade, 0);
    }

    private void inicio(GL2 gl) {
        gl.glTranslatef(0, moverY -= velocidade, 0);
        if (posicaoBolaY <= raqueteY1) {
            resAtual -= pointBatida;
            iniJogo = false;
        }
    }

    private void reset(boolean inicio) {

        
        if (inicio) {
            iniJogo = true;
        } else {
            iniJogo = false;
        }

        pausar = false;

        moverY = 0;
        moverX = 0;
        velocidade = 0.01f;

        bordaSup = 0.85f;
        bordaDir = 0.87f;
        bordaEsq = -1;

        raqueteX1 = -0.2f;
        raqueteX2 = 0.2f;
        raqueteY1 = -0.8f;
        raqueteY2 = -0.9f;

        meioDaTela = 0;
        meioDaTelaLadoDir = -0.5f;
        meioDaTelaLadoDir = 0.5f;

        cX = 0.0f;
        cY = 0.0f;
        rX = 0.05f;
        rY = 0.075f;
        posicaoBolaX = cX + rX;
        posicaoBolaY = cY + rY;

        raquetePontaEsq = raqueteX1;
        raquetePontaDir = raqueteX2;
        moverRaqueteX = 0;

        raqueteMeio = 0;

        subirVetical = false;
        descerVetical = false;
        subirDiagonalDir = false;
        subirDiagonalEsq = false;
        descerDiagonalDir = false;
        descerDiagonalEsq = false;
    }

    private void gameOver() {
        float BOLA_X = 0;
        BOLA_X = (posicaoBolaX + rX);

        if (posicaoBolaY <= raqueteY1
                && BOLA_X < raquetePontaEsq) {

            vida -= 1;
            resAtual -= pointBatida;
            this.reset(true);

            if (vida == 0) {
                opcao = 4;
                vida = 5;
                this.reset(true);
            }
        }

        if (posicaoBolaY <= raqueteY1
                && posicaoBolaX > raquetePontaDir) {

            vida -= 1;
            resAtual -= pointBatida;
            this.reset(true);

            if (vida == 0) {
                opcao = 4;
                vida = 5;
                this.reset(true);
            }
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();
        // ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); // lê a matriz identidade
        // projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-1, 1, -1, 1, -1, 1);
        // ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_RIGHT:
                if (raquetePontaDir < 1) {
                    moverRaqueteX += 0.1f;
                    raquetePontaEsq = raqueteX1 + (moverRaqueteX);
                    raquetePontaDir = raqueteX2 + (moverRaqueteX);
                }
                break;

            case KeyEvent.VK_LEFT:
                if (raquetePontaEsq > -1) {
                    moverRaqueteX -= 0.1f;
                    raquetePontaEsq = raqueteX1 + (moverRaqueteX);
                    raquetePontaDir = raqueteX2 + (moverRaqueteX);
                }
                break;
            case KeyEvent.VK_TAB:
                opcao = 1;
                break;
            case KeyEvent.VK_ENTER:
                opcao = 2;
                break;
            case KeyEvent.VK_P:
                pausar = !pausar;
                if (pausar) {
                    opcao = 3;
                } else {
                    opcao = 2;
                }
                break;
            case KeyEvent.VK_V:
                opcao = 0;
                break;
        }
    }
    
    public void iluminacaoEspecular(GL2 gl){        
        float luzAmbiente[] = {0.2f, 0.2f, 0.2f, 1.0f}; //cor
        float luzEspecular[]={1.0f, 1.0f, 1.0f, 1.0f}; //cor
        float posicaoLuz[]={-50.f, 0.0f, 100.0f, 1.0f}; //pontual
             
        //intensidade da reflexao do material        
        int especMaterial = 128;
        //define a concentracao do brilho
    	gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, especMaterial);

    	//define a reflect ncia do material
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, luzEspecular, 0);

        //define os par metros de luz de n mero 0 (zero)
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, luzEspecular, 0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    public void ligaLuz(GL2 gl) {
        // habilita a defini  o da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        // habilita o uso da ilumina  o na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz de n mero 0
        gl.glEnable(GL2.GL_LIGHT0);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonaliza  o GOURAUD (default)        
        gl.glShadeModel(tonalizacao);
    }
    
    public void keyReleased(KeyEvent e) {
    }
}
