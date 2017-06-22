#version 120

varying vec3 pass_position;
varying vec3 pass_normal;
varying vec2 pass_texCoord;

uniform vec4 m_Diffuse;
uniform vec4 m_Specular;
uniform float m_Shininess;
uniform sampler2D m_DiffuseMap;

uniform vec3 eyePos;

uniform vec4 g_LightColor;
uniform vec4 g_LightPosition;
uniform vec4 g_LightDirection;
uniform vec4 g_AmbientLightColor;

vec4 calcBaseLight(vec4 lightColor, vec3 position, vec3 normal, vec3 direction, vec3 eyePos)
{
	vec4 diffuseColor = vec4(0.0, 0.0, 0.0, 0.0);
	float diffuseFactor = dot(normal, -direction);
	diffuseColor += lightColor * diffuseFactor;

	vec4 specularColor = vec4(0.0, 0.0, 0.0, 0.0);
	if (diffuseFactor > 0) {
		vec3 viewDirection = normalize(eyePos - position);
		vec3 reflectDirection = normalize(reflect(direction, normal));
		float specular = dot(viewDirection, reflectDirection);
		if(specular > 0) {
			specularColor = lightColor * m_Specular * pow(specular, m_Shininess);
		}
	}

	return diffuseColor + specularColor;
}

vec4 calcLight(vec3 position, vec3 normal, vec3 eyePos)
{
	if (g_LightColor == vec4(0.0, 0.0, 0.0, 0.0)) {
		return g_AmbientLightColor;
	} else if (g_LightPosition.w == -1) { // DirectionalLight
		return g_AmbientLightColor + calcBaseLight(g_LightColor, position, normal, g_LightPosition.xyz, eyePos);
	} else if (g_LightPosition.w != -1) { // PointLight
		vec3 pos = g_LightPosition.xyz;
		float radius = g_LightPosition.w;

		vec3 lightDirection = position - pos;
		float distanceToLight = length(lightDirection);
		lightDirection = normalize(lightDirection);

		float attenuation = 1.0 / (1.0 * pow(distanceToLight, 2));

		return g_AmbientLightColor + calcBaseLight(g_LightColor, position, normal, lightDirection, eyePos) * attenuation;
	}
}

void main()
{
	vec4 color = m_Diffuse;
	
	vec3 position = pass_position;
	vec3 normal = pass_normal;
	vec2 texCoord = pass_texCoord;
	
	color *= texture2D(m_DiffuseMap, texCoord);
	
	color *= calcLight(position, normal, eyePos);
	
	gl_FragColor = color;
}
