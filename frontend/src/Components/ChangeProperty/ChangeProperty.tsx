import { FC, useState } from "react";
import { Property } from "../../Services/services";
import { useLanguages } from "../../Hooks/useLanguages";
import { useTopics } from "../../Hooks/useTopics";
import { useChangeProperty } from "../../Hooks/useChangeProperty";
import PropertyCard from "../PropertyCard/PropertyCard";
import { useErrorPropStore } from "../../store";
import "./ChangeProperty.scss";

interface IPropertyProps {
  property: Property;
}
interface IPropertyItem {
  name: string;
  id: number;
}

const ChangeProperty: FC<IPropertyProps> = ({ property }) => {
  const { isError, setError } = useErrorPropStore();
  const { data: dataLanguages } = useLanguages();
  const { data: dataTopics } = useTopics();
  const [inputValue, setInputValue] = useState("");
  const { mutate: addProp } = useChangeProperty("add");

  const addProperty = (name: string) => {
    const data = {
      property: property,
      name: name,
    };
    setError(false);
    setInputValue("");
    addProp(data);
  };
  return (
    <div>
      <input
        placeholder={`Add ${property}`}
        value={inputValue}
        onChange={(event) => setInputValue(event.target.value)}
      ></input>
      <button onClick={() => addProperty(inputValue)}>Add property</button>
      <div
        className={
          isError ? "changeProperty__body error" : "changeProperty__body"
        }
      >
        {isError && <p>{`${property} has dependencies`}</p>}
        {property === "topic"
          ? dataTopics?.data.map((item: IPropertyItem) => (
              <PropertyCard
                key={item.id}
                name={item.name}
                id={item.id}
                property="topic"
              />
            ))
          : dataLanguages?.data.map((item: IPropertyItem) => (
              <PropertyCard
                key={item.id}
                name={item.name}
                id={item.id}
                property="programming-language"
              />
            ))}
      </div>
    </div>
  );
};

export default ChangeProperty;
