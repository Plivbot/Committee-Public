#version 120

uniform sampler2D texture;
uniform vec2 texelSize;

uniform vec4 colour;
uniform float radius;

uniform float time;

#define iterations 17
#define formuparam 0.53

#define volsteps 20
#define stepsize 0.1

#define zoom   4.0
#define tile   50.0
#define speed  0.05

#define brightness 0.0015
#define darkmatter 0.300
#define distfading 0.730
#define saturation 1.0

void main(void) { // https://www.shadertoy.com/view/XlfGRj

    vec4 centerCol = texture2D(texture, gl_TexCoord[0].xy);

    if (centerCol.a > 0) {
        //get coords and direction
        vec2 uv = gl_FragCoord.xy / vec2(1, 1)-.5;
        vec3 dir=vec3(uv*zoom,1.);
        float timeValue=time*speed+.25;

        float a1=0.25;
        float a2=0.40;
        mat2 rot1=mat2(cos(a1),sin(a1),-sin(a1),cos(a1));
        mat2 rot2=mat2(cos(a2),sin(a2),-sin(a2),cos(a2));
        dir.xz*=rot1;
        dir.xy*=rot2;
        vec3 from=vec3(1.,.5,0.5);
        from+=vec3(timeValue*2.,timeValue,-2.);
        from.xz*=rot1;
        from.xy*=rot2;

        //volumetric rendering
        float s=0.1,fade=1.;
        vec3 v=vec3(0.);
        for (int r=0; r<volsteps; r++) {
            vec3 p=from+s*dir*.5;
            p = abs(vec3(tile)-mod(p,vec3(tile*2.))); // tiling fold
            float pa,a=pa=0.;
            for (int i=0; i<iterations; i++) {
                p=abs(p)/dot(p,p)-formuparam; // the magic formula
                a+=abs(length(p)-pa); // absolute sum of average change
                pa=length(p);
            }
            float dm=max(0.,darkmatter-a*a*.001); //dark matter
            a*=a*a; // add contrast
            if (r>6) fade*=1.-dm; // dark matter, don't render near
            //v+=vec3(dm,dm*.5,0.);
            v+=fade;
            v+=vec3(s,s*s,s*s*s*s)*a*brightness*fade; // coloring based on distance
            fade*=distfading; // distance fading
            s+=stepsize;
        }
        v=mix(vec3(length(v)),v,saturation); //color adjust
        gl_FragColor = vec4(v*0.01, 0.8);

    } else {
        for (float x = -radius; x <= radius; x++) {
            for (float y = -radius; y <= radius; y++) {
                vec4 currentColor = texture2D(texture, gl_TexCoord[0].xy + vec2(texelSize.x * x, texelSize.y * y));

                if (currentColor.a > 0) {
                    gl_FragColor = colour;
                }
            }
        }
    }



}