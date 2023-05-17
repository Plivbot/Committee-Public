package me.committee.api.shader;

import com.google.common.base.Charsets;
import me.committee.Committee;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL20;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glUseProgram;


/**
 * Thanks to linustouchtips for the help with understanding how to write this!
 */
public abstract class Shader {

    private int programId;
    private Map<String, Integer> uniforms;

    public Shader(String fragmentPath) {
        int vertexShader = 0;
        int fragmentShader = 0;

        try {

            final InputStream vertexStream = Committee.class.getResourceAsStream("/assets/committee/shaders/vertex.vert");
            final InputStream fragmentStream = Committee.class.getResourceAsStream(fragmentPath);
            if (vertexStream != null && fragmentStream != null) {
                vertexShader = createShader(IOUtils.toString(vertexStream, Charsets.UTF_8), ARBVertexShader.GL_VERTEX_SHADER_ARB);
                fragmentShader = createShader(IOUtils.toString(fragmentStream, Charsets.UTF_8), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

                IOUtils.closeQuietly(vertexStream);
                IOUtils.closeQuietly(fragmentStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (vertexShader != 0 && fragmentShader != 0) {
            this.programId = ARBShaderObjects.glCreateProgramObjectARB();
            if (this.programId != 0) {
                ARBShaderObjects.glAttachObjectARB(this.programId, vertexShader);
                ARBShaderObjects.glAttachObjectARB(this.programId, fragmentShader);

                ARBShaderObjects.glLinkProgramARB(this.programId);
                ARBShaderObjects.glValidateProgramARB(this.programId);
            }
        }

    }

    public void setupUniforms() {
        setupUniform("divider");
        setupUniform("maxSample");
    }

    public abstract void updateUniforms();

    public void draw() {
        glUseProgram(this.programId);

        if (this.uniforms == null) {
            this.uniforms = new HashMap<>();
            this.setupUniforms();
        }

        this.updateUniforms();
    }

    public void setupUniform(String uniformName) {
        this.uniforms.put(uniformName, GL20.glGetUniformLocation(this.programId, uniformName));
    }

    public int getUniform(String uniformName) {
        return this.uniforms.get(uniformName);
    }

    private int createShader(String filePath, int shaderType) {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if (shader == 0)
                return 0;

            ARBShaderObjects.glShaderSourceARB(shader, filePath);
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
                throw new RuntimeException("Error creating shader: " + shader);

            return shader;
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw e;
        }
    }

}
