package com.mn.tiger.bluetooth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 15/8/2.
 */
public class TGBLEScanParameter
{
    private List<TGBLEServiceParameter> serviceParameters;

    private String peripheralUUID;

    public TGBLEScanParameter()
    {
        serviceParameters = new ArrayList<TGBLEServiceParameter>();
    }

    public void addService(TGBLEServiceParameter serviceParameter)
    {
        serviceParameters.add(serviceParameter);
    }

    public void setPeripheralUUID(String peripheralUUID)
    {
        this.peripheralUUID = peripheralUUID;
    }

    public String getPeripheralUUID()
    {
        return peripheralUUID;
    }

    public static class TGBLEServiceParameter
    {
        private List<TGBLECharacteristicParameter> characteristicParameters;

        public TGBLEServiceParameter()
        {
            this.characteristicParameters = new ArrayList<TGBLECharacteristicParameter>();
        }

        public void addCharacteristicParameter(TGBLECharacteristicParameter characteristicParameter)
        {
            characteristicParameters.add(characteristicParameter);
        }
    }

    public static class TGBLECharacteristicParameter
    {
        private int UUID;

        private List<Integer> values;

        public int getUUID()
        {
            return UUID;
        }

        public void setUUID(int UUID)
        {
            this.UUID = UUID;
        }

        public List<Integer> getValues()
        {
            return values;
        }

        public void setValues(List<Integer> values)
        {
            this.values = values;
        }
    }
}
