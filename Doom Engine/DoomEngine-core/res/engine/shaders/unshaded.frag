#version 120

varying vec3 pass_position;
varying vec3 pass_normal;
varying vec2 pass_texCoord;

uniform vec4 m_Diffuse;
uniform sampler2D m_DiffuseMap;

void main()
{
	vec4 color = m_Diffuse;
	vec3 normal = pass_normal;
	
	color *= texture2D(m_DiffuseMap, pass_texCoord);
	
	gl_FragColor = color;
}