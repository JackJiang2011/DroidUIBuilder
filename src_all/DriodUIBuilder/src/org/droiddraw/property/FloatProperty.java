package org.droiddraw.property;

public class FloatProperty extends Property
{
	float def;
	float value;

	public FloatProperty(String englishName, String attName, float def)
	{
		super(englishName, attName, true, true);
		this.def = def;
		this.value = def;
	}

	@Override
	public Object getValue()
	{
		return new Float(value);
	}

	@Override
	protected boolean isDefaultInternal()
	{
		return value == def;
	}

	@Override
	public void setValue(String value)
	{
		try
		{
			this.value = Float.parseFloat(value);
		}
		catch (NumberFormatException ex)
		{
			ex.printStackTrace();
			this.value = -1f;
		}
	}

	public void setFloatValue(float value)
	{
		this.value = value;
	}
}
