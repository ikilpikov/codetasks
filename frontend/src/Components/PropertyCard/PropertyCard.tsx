import { FC, useState } from "react";
import { IChangeProperty } from "../../Services/services";
import { useChangeProperty } from "../../Hooks/useChangeProperty";
import pencil from "../../img/pencil.svg";
import recycleBin from "../../img/recycleBin.svg";
import "./PropertyCard.scss";

const PropertyCard: FC<IChangeProperty> = ({ name, id, property }) => {
  const { mutate: deleteProp } = useChangeProperty("delete");
  const { mutate: updateProp } = useChangeProperty("update");
  const [textareaIsDisabled, setTextareaIsDisabled] = useState(true);
  const [textareaValue, setTextareaValue] = useState(name);

  const removeProperty = () => {
    const data = {
      property: property,
      id: id,
    };
    deleteProp(data);
  };

  const updateProperty = () => {
    if (!textareaIsDisabled) {
      const data = {
        property: property,
        id: id,
        name: textareaValue,
      };
      updateProp(data);
      setTextareaIsDisabled(true);
    } else setTextareaIsDisabled(false);
  };

  return (
    <div className="propertyCard">
      <textarea
        value={textareaValue}
        disabled={textareaIsDisabled}
        onChange={(event) => setTextareaValue(event.target.value)}
      ></textarea>
      <div className="propertyCard__actions">
        <img src={pencil} width="40px" onClick={() => updateProperty()}></img>
        <img
          src={recycleBin}
          width="40px"
          onClick={() => removeProperty()}
        ></img>
      </div>
    </div>
  );
};

export default PropertyCard;
