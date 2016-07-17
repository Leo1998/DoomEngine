#version 120

varying vec3 pass_position;
varying vec3 pass_normal;
varying vec2 pass_texCoord;

uniform vec4 m_Ambient;
uniform vec4 m_Diffuse;
uniform vec4 m_Specular;
uniform float m_Shininess;

uniform sampler2D m_DiffuseMap;
uniform bool m_DiffuseMap_bound;

uniform sampler2D m_SpecularMap;
uniform bool m_SpecularMap_bound;

uniform sampler2D m_NormalMap;
uniform bool m_NormalMap_bound;

uniform sampler2D m_AlphaMap;
uniform bool m_AlphaMap_bound;

void main() {
	vec4 color = m_Diffuse;
	vec3 normal = pass_normal;
	
	if (m_DiffuseMap_bound == true) {
		color = texture2D(m_DiffuseMap, pass_texCoord);
	}
	
	gl_FragColor = color;
}