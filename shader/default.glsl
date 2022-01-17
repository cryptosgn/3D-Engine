#version 460

uniform mat4 uProj;
uniform mat4 uView;

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

out vec4 fColor;

void main()
{
    fColor = aColor;
    gl_Position = uView * uProj * vec4(aPos, 1.0f);
}

#version 460

in vec4 fColor;

out vec4 color;

void main()
{
    color = fColor;
}