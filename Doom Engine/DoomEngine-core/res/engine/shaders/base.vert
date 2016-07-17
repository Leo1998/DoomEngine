#version 120

attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;

uniform mat4 model, viewProjection;
uniform vec3 eyePos;

varying vec3 pass_position;
varying vec3 pass_normal;
varying vec2 pass_texCoord;

void main() {
	pass_position = (model * vec4(a_position, 1.0)).xyz;
	pass_normal = mat3(model) * a_normal;
	pass_texCoord = a_texCoord;
	
    gl_Position = viewProjection * model * vec4(a_position, 1.0);
}