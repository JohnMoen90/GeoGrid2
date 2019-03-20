#version 330

layout (location=0) in vec2 position;
layout (location=1) in vec3 color;
layout (location=2) in int hgt;
layout (location=3) in float hgtColor;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
//uniform float maxHgt;
uniform vec2 mousePos;



out VS_OUT {
    vec3 color;
} vs_out;

float levelRange(int shgt, int miHgt, float maHgt){
    return ((shgt - miHgt) / (maHgt - miHgt));
}

void main()
{
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 0.0, 1.0);
    //vec4 mousePosition = vec4(mousePos, gl_position.z, 1.0) / projectionMatrix;
    //vec4 mousePosition = mousePosition / worldMatrix;
    vs_out.color = vec3(hgtColor - .2,(hgtColor - .2) + .2,.2-hgtColor);
}