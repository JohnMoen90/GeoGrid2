#version 330 core
layout(points) in;
layout (triangle_strip, max_vertices = 4) out;

in VS_OUT {
    vec3 color;
} gs_in[];

out vec3 fColor;

uniform float tileSize;


void build_quad(vec4 position)
{
    fColor = gs_in[0].color;
   gl_Position = position + vec4(-tileSize, -tileSize, 0.0, 0.0);    // 1:bottom-left
       EmitVertex();
       gl_Position = position + vec4( tileSize, -tileSize, 0.0, 0.0);    // 2:bottom-right
       EmitVertex();
       gl_Position = position + vec4(-tileSize,  tileSize, 0.0, 0.0);    // 3:top-left
       EmitVertex();
       gl_Position = position + vec4( tileSize,  tileSize, 0.0, 0.0);    // 4:top-right
       EmitVertex();
       EndPrimitive();
}


void main() {
    build_quad(gl_in[0].gl_Position);
}