#version 330
#extension GL_ARB_separate_shader_objects : require

// position_tex_color.vsh (26.3 layout), but the skybox is pushed onto the far plane so the depth test
// only lets it through where nothing was drawn (the sky).
layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    mat4 TextureMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
};
layout(std140) uniform Projection {
    mat4 ProjMat;
};

layout(location = 0) in vec3 Position;
layout(location = 1) in vec2 UV0;
layout(location = 2) in vec4 Color;

layout(location = 0) out vec2 texCoord0;
layout(location = 1) out vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    // just in front of the cleared depth (1.0), so it is not clipped and still behind every block
    gl_Position.z = gl_Position.w * 0.999999;

    texCoord0 = UV0;
    vertexColor = Color;
}
