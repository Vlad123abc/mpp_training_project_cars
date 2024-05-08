package cars;

import java.util.Objects;

public class Car extends Entity<Long>
{
    private String brand;
    private Integer hp;

    public Car(String brand, Integer hp)
    {
        this.brand = brand;
        this.hp = hp;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public Integer getHp()
    {
        return hp;
    }

    public void setHp(Integer hp)
    {
        this.hp = hp;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Car car = (Car) o;
        return Objects.equals(brand, car.brand) && Objects.equals(hp, car.hp);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), brand, hp);
    }

    @Override
    public String toString()
    {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", hp=" + hp +
                ", id=" + id +
                '}';
    }
}
