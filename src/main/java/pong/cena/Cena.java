package pong.cena;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL;
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
    public static final String IMG_TEXTURA = "src/imagens/fundo.jpg";

    private int opcao = 0;
    private boolean INICIO = true;
    private boolean PAUSE = true;
    
    public int tonalizacao = GL2.GL_SMOOTH; 

    private int PONTUACAO_ATUAL = 0;
    public static int PONTUACAO_FASE_2 = 200;
    public static int PONTOS_POR_BATIDA = 25;
    public static int PONTOS_PARA_VENCER = 400;
    private boolean FASE_2 = true;

    private float MOVE_Y = 0;
    private float MOVE_X = 0;
    private float VELOCIDADE = 0.012f;

    private float LIMITE_SUPERIOR = 0.85f;
    private float LIMITE_DIREITA = 0.87f;
    private float LIMITE_ESQUERDA = -1;

    private float BASTAO_X1 = -0.2f;
    private float BASTAO_X2 = 0.2f;
    private float BASTAO_Y1 = -0.8f;
    private float BASTAO_Y2 = -0.9f;

    private float BASTAO_PONTA_ESQUERDA = BASTAO_X1;
    private float BASTAO_PONTA_DIREITA = BASTAO_X2;
    private float MOVE_BASTAO_X = 0;

    private float BASTAO_CENTRO = 0;
    private float BASTAO_2_CENTRO = 0;

    private float BASTAO_2_X1 = -0.5f;
    private float BASTAO_2_X2 = 0.5f;
    private float BASTAO_2_Y1 = 0.4f;
    private float BASTAO_2_Y2 = 0.3f;

    private float BASTAO_2_PONTA_ESQUERDA = BASTAO_2_X1;
    private float BASTAO_2_PONTA_DIREITA = BASTAO_2_X2;

    private float CENTRO_TELA = 0.0f;
    private float MEIO_CENTRO_TELA_ESQUERDO = -0.5f;
    private float MEIO_CENTRO_TELA_DIREITO = 0.5f;

    private float cX = 0.0f;
    private float cY = 0.0f;
    private float rX = 0.05f;
    private float rY = 0.075f;
    private float POSICAO_BOLA_X = cX + rX;
    private float POSICAO_BOLA_Y = cY + rY;

    private boolean SUBIR_RETO = false;
    private boolean DESCER_RETO = false;
    private boolean SUBIR_DIREITA = false;
    private boolean SUBIR_ESQUERDA = false;
    private boolean DESCER_DIREITA = false;
    private boolean DESCER_ESQUERDA = false;

    public static int VIDAS = 5;

    private float POSICAO_VIDA_ESQ_X = 0.5f;
    private float POSICAO_VIDA_ESQ_Y = 0.9f;

    private float POSICAO_VIDA_Z = 0.3f;

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

                if (INICIO) {
                    this.gameOver();
                    this.inicio(gl);
                }
                this.gameOver();

                if (PONTUACAO_ATUAL >= PONTUACAO_FASE_2) {
                    this.menu.jogo("Segunda Fase");
                } else {
                    this.menu.jogo("Primeira Fase");
                }

                if (PONTUACAO_ATUAL == PONTUACAO_FASE_2 && FASE_2) {
                    opcao = 5;
                    this.reset(false);
                    FASE_2 = false;
                    DESCER_RETO = true;
                }

                POSICAO_BOLA_Y = -(cY + rY) + MOVE_Y;
                POSICAO_BOLA_X = -(cX + rX) + MOVE_X;

                if (PONTUACAO_ATUAL >= PONTUACAO_FASE_2) {
                    VELOCIDADE = 0.015f;

                    BASTAO_2_CENTRO = (BASTAO_2_PONTA_ESQUERDA + BASTAO_2_PONTA_DIREITA) / 2;
                    float BOLA_X = (POSICAO_BOLA_X + rX);
                    float BASTAO_Y2 = (BASTAO_2_Y2 - 0.15f);

                    if (SUBIR_DIREITA || SUBIR_ESQUERDA || SUBIR_RETO) {

                        BASTAO_2_X1 = -0.5f;
                        BASTAO_2_X2 = 0.5f;

                        BASTAO_2_PONTA_ESQUERDA = BASTAO_2_X1;
                        BASTAO_2_PONTA_DIREITA = BASTAO_2_X2;

                        if (POSICAO_BOLA_Y >= BASTAO_Y2 && POSICAO_BOLA_Y < BASTAO_2_Y1
                                && BOLA_X >= BASTAO_2_PONTA_ESQUERDA
                                && BOLA_X <= BASTAO_2_PONTA_DIREITA) {


                            if (POSICAO_BOLA_X == -rX || POSICAO_BOLA_X == BASTAO_2_CENTRO && SUBIR_RETO) {
                                DESCER_RETO = true;
                                SUBIR_ESQUERDA = false;
                                SUBIR_DIREITA = false;
                                SUBIR_RETO = false;
                            }

                            if (SUBIR_ESQUERDA) {
                                DESCER_ESQUERDA = true;
                                SUBIR_ESQUERDA = false;
                                SUBIR_DIREITA = false;
                                SUBIR_RETO = false;
                            }
                            if (SUBIR_DIREITA) {
                                DESCER_DIREITA = true;
                                SUBIR_ESQUERDA = false;
                                SUBIR_DIREITA = false;
                                SUBIR_RETO = false;
                            }
                        }
                    }

                    if (DESCER_DIREITA || DESCER_ESQUERDA) {

                        BASTAO_2_X1 = -0.3f;
                        BASTAO_2_X2 = 0.3f;

                        BASTAO_2_PONTA_ESQUERDA = BASTAO_2_X1;
                        BASTAO_2_PONTA_DIREITA = BASTAO_2_X2;

                        if (POSICAO_BOLA_Y <= BASTAO_2_Y1 && POSICAO_BOLA_Y > BASTAO_2_Y2
                                && BOLA_X >= BASTAO_2_PONTA_ESQUERDA
                                && BOLA_X <= BASTAO_2_PONTA_DIREITA) {


                            if (DESCER_DIREITA) {
                                SUBIR_DIREITA = true;
                                DESCER_RETO = false;
                                DESCER_ESQUERDA = false;
                                DESCER_DIREITA = false;
                            }
                            if (DESCER_ESQUERDA) {
                                SUBIR_ESQUERDA = true;
                                DESCER_RETO = false;
                                DESCER_ESQUERDA = false;
                                DESCER_DIREITA = false;
                            }
                        }
                    }

                    if (PONTUACAO_ATUAL == PONTOS_PARA_VENCER) {
                        opcao = 6;
                        this.reset(true);
                        PONTUACAO_ATUAL = 0;
                        VIDAS = 5;
                        FASE_2 = true;
                    }
                }


                if (POSICAO_BOLA_Y <= BASTAO_Y1) {
                    DESCER_ESQUERDA = false;
                    DESCER_DIREITA = false;
                    DESCER_RETO = false;

                    PONTUACAO_ATUAL += PONTOS_POR_BATIDA;

                    BASTAO_CENTRO = (BASTAO_PONTA_ESQUERDA + BASTAO_PONTA_DIREITA) / 2;

                    if (POSICAO_BOLA_X == -rX && BASTAO_CENTRO == CENTRO_TELA || POSICAO_BOLA_X == BASTAO_CENTRO) {
                        SUBIR_RETO = true;
                    }

                    if (POSICAO_BOLA_X > BASTAO_CENTRO) {
                        if (BASTAO_CENTRO < CENTRO_TELA && BASTAO_CENTRO >= MEIO_CENTRO_TELA_ESQUERDO) {
                            SUBIR_DIREITA = true;
                        }

                        if (BASTAO_CENTRO < CENTRO_TELA && BASTAO_CENTRO < MEIO_CENTRO_TELA_ESQUERDO) {
                            SUBIR_ESQUERDA = true;
                        }

                        if (BASTAO_CENTRO > CENTRO_TELA && BASTAO_CENTRO <= MEIO_CENTRO_TELA_DIREITO) {
                            SUBIR_DIREITA = true;
                        }

                        if (BASTAO_CENTRO > CENTRO_TELA && BASTAO_CENTRO > MEIO_CENTRO_TELA_DIREITO) {
                            SUBIR_ESQUERDA = true;
                        }
                    }

                    if (POSICAO_BOLA_X < BASTAO_CENTRO) {
                        if (BASTAO_CENTRO < CENTRO_TELA && BASTAO_CENTRO > MEIO_CENTRO_TELA_ESQUERDO) {
                            SUBIR_ESQUERDA = true;
                        }

                        if (BASTAO_CENTRO < CENTRO_TELA && BASTAO_CENTRO <= MEIO_CENTRO_TELA_ESQUERDO) {
                            SUBIR_DIREITA = true;
                        }

                        if (BASTAO_CENTRO > CENTRO_TELA && BASTAO_CENTRO <= MEIO_CENTRO_TELA_DIREITO) {
                            SUBIR_ESQUERDA = true;
                        }

                        if (BASTAO_CENTRO > CENTRO_TELA && BASTAO_CENTRO > MEIO_CENTRO_TELA_DIREITO) {
                            SUBIR_DIREITA = true;
                        }
                    }
                }

                if (SUBIR_ESQUERDA) {
                    this.subirEsquerda(gl);
                }

                if (SUBIR_DIREITA) {
                    this.subirDireita(gl);
                }

                if (DESCER_ESQUERDA) {
                    this.descerEsquerda(gl);
                }

                if (DESCER_DIREITA) {
                    this.descerDireita(gl);
                }

                if (SUBIR_RETO) {
                    this.subirReto(gl);
                }

                if (DESCER_RETO) {
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
                gl.glTranslatef(MOVE_BASTAO_X, 0, 0);
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2d(BASTAO_X1, BASTAO_Y1);
                gl.glVertex2d(BASTAO_X2, BASTAO_Y1);
                gl.glVertex2d(BASTAO_X2, BASTAO_Y2);
                gl.glVertex2d(BASTAO_X1, BASTAO_Y2);


                gl.glEnd();
                gl.glPopMatrix();

                if (PONTUACAO_ATUAL >= PONTUACAO_FASE_2) {
                    // BASTAO 2
                    gl.glPushMatrix();
                    gl.glColor3f(1, 1, 1);
                    gl.glBegin(GL2.GL_QUADS);
                    gl.glVertex2d(BASTAO_2_X1, BASTAO_2_Y1);
                    gl.glVertex2d(BASTAO_2_X2, BASTAO_2_Y1);
                    gl.glVertex2d(BASTAO_2_X2, BASTAO_2_Y2);
                    gl.glVertex2d(BASTAO_2_X1, BASTAO_2_Y2);
                    gl.glEnd();
                    gl.glPopMatrix();
                }

                float incr = 0;
                for (int i = 0; i < VIDAS; i += 1) {
                    desenhaVida(gl, glut, incr);
                    incr += 0.1f;
                }
                this.menu.pontos(PONTUACAO_ATUAL);
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
        gl.glTranslatef(POSICAO_VIDA_ESQ_X + incr, POSICAO_VIDA_ESQ_Y, POSICAO_VIDA_Z);

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
        gl.glTranslatef(POSICAO_VIDA_ESQ_X + incr, POSICAO_VIDA_ESQ_Y + tamanho * 1.3f, POSICAO_VIDA_Z);

        float tamanhoOlho = tamanho * 0.2f;

        gl.glColor3f(0, 0, 0); // Cor preta para os olhos

        glut.glutSolidSphere(tamanhoOlho, 10, 10);

        gl.glTranslatef(tamanho * 0.3f, 0, 0);
        glut.glutSolidSphere(tamanhoOlho, 10, 10);

        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    private void subirDireita(GL2 gl) {
        gl.glTranslatef(MOVE_X += VELOCIDADE, MOVE_Y += VELOCIDADE, 0);
        if (POSICAO_BOLA_X >= LIMITE_DIREITA) {
            SUBIR_ESQUERDA = true;
            SUBIR_DIREITA = false;
            SUBIR_RETO = false;
            DESCER_ESQUERDA = false;
            DESCER_DIREITA = false;
            DESCER_RETO = false;
        }

        if (POSICAO_BOLA_Y >= LIMITE_SUPERIOR) {
            DESCER_DIREITA = true;
            SUBIR_RETO = false;
            SUBIR_DIREITA = false;
            SUBIR_ESQUERDA = false;
            DESCER_ESQUERDA = false;
            DESCER_RETO = false;
        }
    }

    private void subirEsquerda(GL2 gl) {
        gl.glTranslatef(MOVE_X -= VELOCIDADE, MOVE_Y += VELOCIDADE, 0);
        if (POSICAO_BOLA_X <= LIMITE_ESQUERDA) {
            SUBIR_DIREITA = true;
            SUBIR_ESQUERDA = false;
            SUBIR_RETO = false;
            DESCER_RETO = false;
            DESCER_DIREITA = false;
            DESCER_ESQUERDA = false;
        }

        if (POSICAO_BOLA_Y >= LIMITE_SUPERIOR) {
            DESCER_ESQUERDA = true;
            SUBIR_ESQUERDA = false;
            SUBIR_DIREITA = false;
            SUBIR_RETO = false;
            DESCER_DIREITA = false;
            DESCER_RETO = false;
        }
    }

    private void descerEsquerda(GL2 gl) {
        gl.glTranslatef(MOVE_X -= VELOCIDADE, MOVE_Y -= VELOCIDADE, 0);
        if (POSICAO_BOLA_X <= LIMITE_ESQUERDA) {
            DESCER_DIREITA = true;
            DESCER_ESQUERDA = false;
            DESCER_RETO = false;
            SUBIR_RETO = false;
            SUBIR_DIREITA = false;
            SUBIR_ESQUERDA = false;
        }
    }

    private void descerDireita(GL2 gl) {
        gl.glTranslatef(MOVE_X += VELOCIDADE, MOVE_Y -= VELOCIDADE, 0);
        if (POSICAO_BOLA_X >= LIMITE_DIREITA) {
            DESCER_ESQUERDA = true;
            DESCER_DIREITA = false;
            DESCER_RETO = false;
            SUBIR_RETO = false;
            SUBIR_DIREITA = false;
            SUBIR_ESQUERDA = false;
        }
    }

    private void subirReto(GL2 gl) {
        gl.glTranslatef(0, MOVE_Y += VELOCIDADE, 0);
        if (POSICAO_BOLA_Y >= LIMITE_SUPERIOR) {
            DESCER_RETO = true;
            SUBIR_RETO = false;
            SUBIR_DIREITA = false;
            SUBIR_ESQUERDA = false;
            DESCER_DIREITA = false;
            DESCER_ESQUERDA = false;
        }
    }

    private void descerReto(GL2 gl) {
        gl.glTranslatef(0, MOVE_Y -= VELOCIDADE, 0);
    }

    private void inicio(GL2 gl) {
        gl.glTranslatef(0, MOVE_Y -= VELOCIDADE, 0);
        if (POSICAO_BOLA_Y <= BASTAO_Y1) {
            PONTUACAO_ATUAL -= PONTOS_POR_BATIDA;
            INICIO = false;
        }
    }

    private void reset(boolean inicio) {

        if (inicio) {
            INICIO = true;
        } else {
            INICIO = false;
        }

        PAUSE = false;

        MOVE_Y = 0;
        MOVE_X = 0;
        VELOCIDADE = 0.01f;

        LIMITE_SUPERIOR = 0.85f;
        LIMITE_DIREITA = 0.87f;
        LIMITE_ESQUERDA = -1;

        BASTAO_X1 = -0.2f;
        BASTAO_X2 = 0.2f;
        BASTAO_Y1 = -0.8f;
        BASTAO_Y2 = -0.9f;

        CENTRO_TELA = 0;
        MEIO_CENTRO_TELA_ESQUERDO = -0.5f;
        MEIO_CENTRO_TELA_DIREITO = 0.5f;

        cX = 0.0f;
        cY = 0.0f;
        rX = 0.05f;
        rY = 0.075f;
        POSICAO_BOLA_X = cX + rX;
        POSICAO_BOLA_Y = cY + rY;

        BASTAO_PONTA_ESQUERDA = BASTAO_X1;
        BASTAO_PONTA_DIREITA = BASTAO_X2;
        MOVE_BASTAO_X = 0;

        BASTAO_CENTRO = 0;

        SUBIR_RETO = false;
        DESCER_RETO = false;
        SUBIR_DIREITA = false;
        SUBIR_ESQUERDA = false;
        DESCER_DIREITA = false;
        DESCER_ESQUERDA = false;
    }

    private void gameOver() {
        float BOLA_X = 0;
        BOLA_X = (POSICAO_BOLA_X + rX);

        if (POSICAO_BOLA_Y <= BASTAO_Y1
                && BOLA_X < BASTAO_PONTA_ESQUERDA) {

            VIDAS -= 1;
            PONTUACAO_ATUAL -= PONTOS_POR_BATIDA;
            this.reset(true);

            if (VIDAS == 0) {
                opcao = 4;
                VIDAS = 5;
                this.reset(true);
            }
        }

        if (POSICAO_BOLA_Y <= BASTAO_Y1
                && POSICAO_BOLA_X > BASTAO_PONTA_DIREITA) {

            VIDAS -= 1;
            PONTUACAO_ATUAL -= PONTOS_POR_BATIDA;
            this.reset(true);

            if (VIDAS == 0) {
                opcao = 4;
                VIDAS = 5;
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
                if (BASTAO_PONTA_DIREITA < 1) {
                    MOVE_BASTAO_X += 0.1f;
                    BASTAO_PONTA_ESQUERDA = BASTAO_X1 + (MOVE_BASTAO_X);
                    BASTAO_PONTA_DIREITA = BASTAO_X2 + (MOVE_BASTAO_X);
                }
                break;

            case KeyEvent.VK_LEFT:
                if (BASTAO_PONTA_ESQUERDA > -1) {
                    MOVE_BASTAO_X -= 0.1f;
                    BASTAO_PONTA_ESQUERDA = BASTAO_X1 + (MOVE_BASTAO_X);
                    BASTAO_PONTA_DIREITA = BASTAO_X2 + (MOVE_BASTAO_X);
                }
                break;
            case KeyEvent.VK_TAB:
                opcao = 1;
                break;
            case KeyEvent.VK_ENTER:
                opcao = 2;
                break;
            case KeyEvent.VK_P:
                PAUSE = !PAUSE;
                if (PAUSE) {
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

    	//define a reflect�ncia do material
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, luzEspecular, 0);

        //define os par�metros de luz de n�mero 0 (zero)
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, luzEspecular, 0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
    }
    
    public void ligaLuz(GL2 gl) {
        // habilita a defini��o da cor do material a partir da cor corrente
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        // habilita o uso da ilumina��o na cena
        gl.glEnable(GL2.GL_LIGHTING);
        // habilita a luz de n�mero 0
        gl.glEnable(GL2.GL_LIGHT0);
        //Especifica o Modelo de tonalizacao a ser utilizado 
        //GL_FLAT -> modelo de tonalizacao flat 
        //GL_SMOOTH -> modelo de tonaliza��o GOURAUD (default)        
        gl.glShadeModel(tonalizacao);
    }
    
    public void keyReleased(KeyEvent e) {
    }
}
