#version 120

attribute vec2 a_position;
attribute float a_tid;
attribute vec4 a_color;
attribute vec2 a_texCoord;

uniform mat4 projectionMatrix;

varying vec4 v_color;
varying float v_tid;
varying vec2 v_texCoord;

void main() {
	v_color = color;
	v_tid = tid;
	v_texCoord = texCoord;
	gl_Position = vec4(position.xy, 0.0, 1.0) * projectionMatrix;
}