package com.mn.tiger.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by peng on 15/8/2.
 */
public class TGBLEScanParameter
{
    private List<TGBLEServiceParameter> serviceParameters;

    private UUID peripheralUUID;

    public TGBLEScanParameter()
    {
        serviceParameters = new ArrayList<TGBLEServiceParameter>();
    }

    public void addService(TGBLEServiceParameter serviceParameter)
    {
        serviceParameters.add(serviceParameter);
    }

    public void setPeripheralUUID(UUID peripheralUUID)
    {
        this.peripheralUUID = peripheralUUID;
    }

    public UUID getPeripheralUUID()
    {
        return peripheralUUID;
    }

    public UUID[] getServiceUUIDs()
    {
        UUID[] uuids = new UUID[serviceParameters.size()];
        for (int i = 0; i < serviceParameters.size(); i++)
        {
            uuids[i] = serviceParameters.get(i).getUUID();
        }

        return uuids;
    }

    public static class TGBLEServiceParameter
    {
        private UUID UUID;

        private List<TGBLECharacteristicParameter> characteristicParameters;

        public TGBLEServiceParameter()
        {
            this.characteristicParameters = new ArrayList<TGBLECharacteristicParameter>();
        }

        public java.util.UUID getUUID()
        {
            return UUID;
        }

        public void setUUID(java.util.UUID UUID)
        {
            this.UUID = UUID;
        }

        public void addCharacteristicParameter(TGBLECharacteristicParameter characteristicParameter)
        {
            characteristicParameters.add(characteristicParameter);
        }
    }

    public static class TGBLECharacteristicParameter
    {
        private UUID UUID;

        private List<Integer> values;

        public void setUUID(java.util.UUID UUID)
        {
            this.UUID = UUID;
        }

        public java.util.UUID getUUID()
        {
            return UUID;
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
