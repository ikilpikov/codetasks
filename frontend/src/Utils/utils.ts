import { Difficulties } from "../Services/services";

export const createRequest = (
  filterElement: Difficulties | string[] | string | undefined,
  elementType: string
) => {
  if (
    filterElement &&
    filterElement?.length > 0 &&
    Array.isArray(filterElement)
  ) {
    let request = "";
    filterElement.map((item) => {
      request += `&${elementType}=` + item;
    });
    return request;
  }
};

export const formatDate = (dateStr: string) => {
  const dateTime = new Date(dateStr);

  const hours = dateTime.getHours().toString().padStart(2, "0");
  const minutes = dateTime.getMinutes().toString().padStart(2, "0");
  const seconds = dateTime.getSeconds().toString().padStart(2, "0");
  const date = dateTime.toISOString().split("T")[0];

  return `${hours}:${minutes}:${seconds} ${date}`;
};

