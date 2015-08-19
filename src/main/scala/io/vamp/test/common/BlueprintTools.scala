package io.vamp.test.common


trait BlueprintTools extends ArtifactTools {
  override def getAll[Blueprint]: List[Blueprint]

  override def getByName[Blueprint]: Blueprint

  override def create[Blueprint](artifact: Blueprint)

  override def update[Blueprint](artifact: Blueprint)

  override def delete[Blueprint](artifact: Blueprint)

}
