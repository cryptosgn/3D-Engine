#type fragment
#version 460

uniform float u_time;
uniform vec2 u_resolution;


vec3 block(){
    vec3 result = vec3(step(0.0,((gl_FragCoord.x-gl_FragCoord.y)/u_resolution.x)));
    result.x=result.x*(sin(u_time*5.0));
    return result;
}

vec2 smoothening(){
    return gl_FragCoord.xy/u_resolution;
}

void main(){
    vec4 color = vec4(block(),1.0);
    gl_FragColor = vec4(vec3(smoothening()),color.x,1.0);
}
