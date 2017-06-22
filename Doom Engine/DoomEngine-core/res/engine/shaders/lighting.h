float specularIntensity;
float specularPower;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct DirectionalLight
{
    BaseLight base;
    vec3 direction;
};

struct PointLight
{
    BaseLight base;
    vec3 position;
};

struct SpotLight
{
    PointLight pointLight;
    vec3 direction;
    float cutoff;
};

vec3 calcBaseLight(BaseLight light, vec3 position, vec3 normal, vec3 direction, vec3 eyePos)
{
	vec3 diffuseColor = vec3(0.0, 0.0, 0.0);
	float diffuseFactor = dot(normal, -direction);
	diffuseColor += light.color * light.intensity * diffuseFactor;
	
	vec3 specularColor = vec3(0.0, 0.0, 0.0);
	if (specularIntensity > 0) {
		vec3 viewDirection = normalize(eyePos - position);
		vec3 reflectDirection = normalize(reflect(direction, normal));
		//vec3 halfDirection = normalize(viewDirection - direction);
		float specular = dot(viewDirection, reflectDirection);
		//float specular = dot(halfDirection, normal);
		if(specular > 0) {
			specularColor = light.color * specularIntensity * pow(specular, specularPower);
		}
	}
	
	return light.color;
	//return diffuseColor + specularColor;
}

vec3 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal, vec3 eyePos)
{
    return calcBaseLight(light.base, position, normal, -light.direction, eyePos);
}

vec3 calcPointLight(PointLight light, vec3 position, vec3 normal, vec3 eyePos)
{
	vec3 lightDirection = position - light.position;
	float distanceToLight = length(lightDirection);
	lightDirection = normalize(lightDirection);
	
	float attenuation = 1.0 / (1.0 * pow(distanceToLight, 2));
	
	vec3 color = calcBaseLight(light.base, position, normal, lightDirection, eyePos);
	
	return color * attenuation;
}

vec3 calcSpotLight(SpotLight light, vec3 position, vec3 normal, vec3 eyePos)
{
    vec3 lightDirection = normalize(position - light.pointLight.position);
    float spotFactor = dot(lightDirection, normalize(light.direction));
    
    vec3 color = vec3(0, 0, 0);
    
    if(spotFactor > light.cutoff)
    {
        color = calcPointLight(light.pointLight, position, normal, eyePos);
    }
    
    return color;
}