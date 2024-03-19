import axios from "axios";
const BASE = import.meta.env.VITE_API_URL;

export const authorization = async (userData: IUserData) => {
  const { userName, password } = userData;
  const response = await axios.post(BASE + "/auth/login", {
    username: userName,
    password: password,
  });
  return response;
};

export type Difficulties = "EASY" | "MEDIUM" | "HARD";
export interface ITasks {
  page?: number;
  size: number;
  difficulties?: Difficulties[] | string;
  topics?: string[] | string;
  languages?: string[] | string;
}

export const getAllTasks = async (request: Partial<ITasks> = {}) => {
  let { difficulties, topics, languages, size } = request;
  difficulties ??= "";
  topics ??= "";
  languages ??= "";
  const response = await axios(
    BASE + `/task/all?page=0&size=${size}${difficulties}${topics}${languages}`,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export interface ITask {
  id: number;
  name: string;
  topic: string;
  difficulty: Difficulties;
  languages: string[];
}

export const getAllLanguages = async () => {
  const response = await axios(BASE + "/programming-language/all", {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};

export const getAllTopics = async () => {
  const response = await axios(BASE + "/topic/all", {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};

export const getTaskCount = async () => {
  const response = await axios(BASE + "/task/count", {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};

export const getTask = async (id: string) => {
  const response = await axios(BASE + `/task/${id}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};

interface IUserData {
  userName: string;
  password: string;
}

export const registration = async (userData: IUserData) => {
  const { userName, password } = userData;

  const response = await axios.post(BASE + "/auth/register", {
    username: userName,
    password: password,
  });

  return response;
};

export interface ILikeComment {
  commentId: number;
  action: string;
}

export const likeComment = async (data: ILikeComment) => {
  const { commentId, action } = data;
  const response = await axios.post(
    BASE + `/task/comment/${action}`,
    { id: commentId },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export interface IComment {
  text: string;
  id: number;
}

export const sendComment = async (data: IComment) => {
  const { text, id } = data;
  const response = await axios.post(
    BASE + `/task/${id}/comment/add`,
    { text: text },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

interface ICode {
  language: string;
  code: string;
}

export interface IExecute extends ICode {
  stdin: string;
}

export const executeCide = async (data: IExecute) => {
  const { language, stdin, code } = data;
  const response = await axios.post(
    BASE + "/solution/execute",
    {
      language: language,
      stdin: stdin,
      code: code,
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export interface IAttempt extends ICode {
  taskId: string | undefined;
}

export const attemptCode = async (data: IAttempt) => {
  const { language, taskId, code } = data;
  const response = await axios.post(
    BASE + "/solution/attempt",
    {
      language: language,
      taskId: taskId,
      code: code,
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export interface ISolution {
  id: number;
  username: string;
  submissionDate: string;
  submissionTime: string;
  code: string;
}

export const getSolutions = async (taskId: number) => {
  const response = await axios(BASE + `/solution/all/${taskId}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};

export const deleteComment = async (commentId: number) => {
  const response = await axios.delete(
    BASE + `/task/comment/delete/${commentId}`,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );

  return response;
};

export type Property = "topic" | "programming-language";
export type Action = "add" | "delete" | "update";
export interface IChangeProperty {
  property: Property;
  id?: number;
  name?: string;
}

export const addProperty = async (data: IChangeProperty) => {
  const { name, property } = data;
  const response = await axios.post(
    BASE + `/${property}/add`,
    {
      name: name,
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export const deleteProperty = async (data: IChangeProperty) => {
  const { property, id } = data;
  const response = await axios.delete(BASE + `/${property}/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};

export const updateProperty = async (data: IChangeProperty) => {
  const { property, id, name } = data;
  const response = await axios.put(
    BASE + `/${property}/update/${id}`,
    {
      name: name,
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export interface ITestCase {
  inputData: string;
  outputData: string;
}
export interface ITaskData {
  name: string;
  condition: string;
  topic: string;
  difficulty: Difficulties;
  languages: string[];
  testcases: ITestCase[];
}

export const addTask = async (data: ITaskData) => {
  const { name, condition, topic, difficulty, languages, testcases } = data;
  const response = await axios.post(
    BASE + "/task/add",
    {
      name,
      condition,
      topic,
      difficulty,
      languages,
      testcases,
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export const deleteTask = async (id: number) => {
  const response = await axios.delete(BASE + `/task/delete/${id}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};

export interface ITaskDataUpdate extends ITaskData {
  id: number;
}
export const updateTask = async (data: ITaskDataUpdate) => {
  const { name, condition, topic, difficulty, languages, testcases, id } = data;
  const response = await axios.put(
    BASE + `/task/update/${id}`,
    {
      name,
      condition,
      topic,
      difficulty,
      languages,
      testcases,
    },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response;
};

export const getTaskForUpdate = async (id: number) => {
  const response = await axios(BASE + `/task/update/${id}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
  return response;
};
