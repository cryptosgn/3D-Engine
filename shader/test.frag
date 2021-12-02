#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;

float smoothie(vec2 x){
    return smoothstep(0.003,0.0,abs(x.y-x.x));
}

float smoothi(vec2 x){
    return smoothstep(0.3,0.0,abs(x.x-x.y));
}

void main(){
    vec2 res = gl_FragCoord.xy/u_resolution;
    float eins = smoothie(res);
    float zwei = smoothi(gl_FragCoord.xy);
    gl_FragColor = vec4(eins,zwei,0.0,1.0);
}