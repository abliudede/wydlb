package com.lianzai.reader.bean;

/**
 * Created by lrz on 2017/10/27.
 */

public class WalletAddressBean extends BaseBean {

    /**
     * data : {"address":"0x6e22483fdbadee8d7e023c958325f5112af1cd71","addressQrcode":"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAIAAAAiOjnJAAAEGElEQVR4nO3dwY4TORRA0aE1///JLRazizQlCr9rV9LnbIEktK6sh3Fcv76/v/+BaV+nPwCfSVgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgk/h1/xa+vTbG+XA7w8r6Dv3rt1p9d+c2d4poFKxYJYZEQFglhkZgf3l8MDobXw+z1PH7rpVZm+Wsr/2jY9pOceYv6DfiZhEVCWCSERSIf3l/cGhtXdsBXXnlwtr0ez1fm8e4nOcKKRUJYJIRFQlgkdg/vz3RrEB48gdNt8R9nxSIhLBLCIiEsEh87vK/srXc7/tuOsR/3U/6ebCYsEsIiISwSu4f3bnN5cFq/ddZlZR5f+Wk8fJveikVCWCSERUJYJPLhfdte88qXPwdPwnSv/F679u/0WXkjwiIhLBLCIjE/vJ/aEd62p39rHh983/dixSIhLBLCIiEsEs+6533l653dLvbK10pXLrAc/CsM3mv/h6xYJIRFQlgkhEVi9z3v3SDczdSn9vQH/xm0/8iNFYuEsEgIi4SwSPwan0xPnePeNq6eGvzf60COFYuEsEgIi4SwSBw+8z54DfpDHtJ06gj8066Mt2KREBYJYZEQFon5nfdbBsfzlQ3xbadKup/2qZM//8eKRUJYJIRFQlgkdh+bOfWl0260X/l3QLeZPvgx/o4Vi4SwSAiLhLBI5Dvv3cWKK5985VN1Q/S1bU+hGmHFIiEsEsIiISwSu5+w+mLbmfdTJ/EH9+VX/oL7T8RbsUgIi4SwSAiLxKNvm+mO3Nz6GCuf6pbB/6U4PstbsUgIi4SwSAiLxO5jMy+6zeVr2y6wvPW+17Z9mWCEFYuEsEgIi4SwSORPWF15DtFbXCCz7WOsnM9x2wwfQlgkhEVCWCR+yj3vt2bblSM3g6d3jt/VvsKKRUJYJIRFQlgk8i+sDt7rsvKbu7n41oe8tRHfXffozDvvSlgkhEVCWCQOP6RpUDeQPvOVH375jBWLhLBICIuEsEjkZ94712ddnvbE0f+s7LwPnnn3hVXelbBICIuEsEgcvipy2yufukDm1PWWx/9DxYpFQlgkhEVCWCR2P6SpO8e97Y0GT7UP/tmVl3JshrchLBLCIiEsEoefsDro1OS7bU9/cKt9w9EmKxYJYZEQFglhkfic4f2W7glHg7frXHvaxe4vrFgkhEVCWCSERWL38H58qPwT2663HHT8epnXd6zfgJ9JWCSERUJYJHbf875Nd+374Gb6Bz/gyYpFQlgkhEVCWCQ+5553HsWKRUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRF4jdVXThdMNMMOwAAAABJRU5ErkJggg=="}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : 0x6e22483fdbadee8d7e023c958325f5112af1cd71
         * addressQrcode : data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAIAAAAiOjnJAAAEGElEQVR4nO3dwY4TORRA0aE1///JLRazizQlCr9rV9LnbIEktK6sh3Fcv76/v/+BaV+nPwCfSVgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgk/h1/xa+vTbG+XA7w8r6Dv3rt1p9d+c2d4poFKxYJYZEQFglhkZgf3l8MDobXw+z1PH7rpVZm+Wsr/2jY9pOceYv6DfiZhEVCWCSERSIf3l/cGhtXdsBXXnlwtr0ez1fm8e4nOcKKRUJYJIRFQlgkdg/vz3RrEB48gdNt8R9nxSIhLBLCIiEsEh87vK/srXc7/tuOsR/3U/6ebCYsEsIiISwSu4f3bnN5cFq/ddZlZR5f+Wk8fJveikVCWCSERUJYJPLhfdte88qXPwdPwnSv/F679u/0WXkjwiIhLBLCIjE/vJ/aEd62p39rHh983/dixSIhLBLCIiEsEs+6533l653dLvbK10pXLrAc/CsM3mv/h6xYJIRFQlgkhEVi9z3v3SDczdSn9vQH/xm0/8iNFYuEsEgIi4SwSPwan0xPnePeNq6eGvzf60COFYuEsEgIi4SwSBw+8z54DfpDHtJ06gj8066Mt2KREBYJYZEQFon5nfdbBsfzlQ3xbadKup/2qZM//8eKRUJYJIRFQlgkdh+bOfWl0260X/l3QLeZPvgx/o4Vi4SwSAiLhLBI5Dvv3cWKK5985VN1Q/S1bU+hGmHFIiEsEsIiISwSu5+w+mLbmfdTJ/EH9+VX/oL7T8RbsUgIi4SwSAiLxKNvm+mO3Nz6GCuf6pbB/6U4PstbsUgIi4SwSAiLxO5jMy+6zeVr2y6wvPW+17Z9mWCEFYuEsEgIi4SwSORPWF15DtFbXCCz7WOsnM9x2wwfQlgkhEVCWCR+yj3vt2bblSM3g6d3jt/VvsKKRUJYJIRFQlgk8i+sDt7rsvKbu7n41oe8tRHfXffozDvvSlgkhEVCWCQOP6RpUDeQPvOVH375jBWLhLBICIuEsEjkZ94712ddnvbE0f+s7LwPnnn3hVXelbBICIuEsEgcvipy2yufukDm1PWWx/9DxYpFQlgkhEVCWCR2P6SpO8e97Y0GT7UP/tmVl3JshrchLBLCIiEsEoefsDro1OS7bU9/cKt9w9EmKxYJYZEQFglhkfic4f2W7glHg7frXHvaxe4vrFgkhEVCWCSERWL38H58qPwT2663HHT8epnXd6zfgJ9JWCSERUJYJHbf875Nd+374Gb6Bz/gyYpFQlgkhEVCWCQ+5553HsWKRUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRFQlgkhEVCWCSERUJYJIRF4jdVXThdMNMMOwAAAABJRU5ErkJggg==
         */

        private String address;
        private String addressQrcode;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressQrcode() {
            return addressQrcode;
        }

        public void setAddressQrcode(String addressQrcode) {
            this.addressQrcode = addressQrcode;
        }
    }
}
