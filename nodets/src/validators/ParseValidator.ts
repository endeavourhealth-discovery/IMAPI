import { Request, Response, NextFunction } from "express";

export function validateTransformInputUpload(req: Request, res: Response, next: NextFunction) {
  if (req.body && req.body.fileString) {
    next();
  } else {
    res.status(400).send({
      error: "Missing required field fileString"
    });
  }
}
