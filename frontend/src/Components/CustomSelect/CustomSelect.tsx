import { FC } from "react";
import Select from "react-select";
import { useTasksStore } from "../../store";

export interface IOption {
  id: number;
  name: string;
}
interface SelectProps {
  data: IOption[];
  type: string;
}
const CustomSelect: FC<SelectProps> = ({ data, type }) => {
  const options = data?.map((element) => ({
    value: element.id,
    label: element.name,
  }));

  const { setTopics, setLanguages } = useTasksStore();
  const selectOnChange = (event: any) => {
    const selectedItems = event.map((element: any) => element.label);
    localStorage.removeItem("position");
    if (type === "topic") setTopics(selectedItems);
    else if (type === "language") setLanguages(selectedItems);
  };
  return (
    <>
      <Select
        isMulti
        options={options}
        onChange={(event) => selectOnChange(event)}
      />
    </>
  );
};

export default CustomSelect;
