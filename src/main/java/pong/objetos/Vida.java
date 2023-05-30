package pong.objetos;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Vida {

    private static Vida instance = null;
    public Vida() {}

    public static Vida getInstance() {
        if(instance == null) {
            instance = new Vida();
        }

        return instance;
    }

    public void desenhaVida(GL2 gl, GLUT glut, float incr) {
        gl.glPushMatrix();
        gl.glColor3f(1, 1, 1);

        gl.glPushMatrix();
        float POSICAO_VIDA_ESQ_X = 0.5f;
        float POSICAO_VIDA_ESQ_Y = 0.9f;
        float POSICAO_VIDA_Z = 0.3f;
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
        gl.glPushMatrix();
        gl.glTranslatef(POSICAO_VIDA_ESQ_X + incr, POSICAO_VIDA_ESQ_Y + tamanho * 1.3f, POSICAO_VIDA_Z);

        float tamanhoOlho = tamanho * 0.2f;

        gl.glColor3f(0, 0, 0);

        glut.glutSolidSphere(tamanhoOlho, 10, 10);

        gl.glTranslatef(tamanho * 0.3f, 0, 0);
        glut.glutSolidSphere(tamanhoOlho, 10, 10);

        gl.glPopMatrix();

        gl.glPopMatrix();
    }
}
