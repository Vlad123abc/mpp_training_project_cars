package cars.jsonProtocol;

public class Response
{
    private ResponseType type;
    private Object data;

    private Response() {}

    public ResponseType getType()
    {
        return type;
    }

    public void setType(ResponseType type)
    {
        this.type = type;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "Response{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }

    public static class Builder
    {
        private Response response = new Response();

        public Builder setType(ResponseType type)
        {
            response.setType(type);
            return this;
        }

        public Builder setData(Object data)
        {
            response.setData(data);
            return this;
        }

        public Response build() {
            return response;
        }
    }
}
